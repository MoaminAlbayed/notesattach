package albayed.moamin.notesattach.screens.locations

import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    noteId: String,
    viewModel: LocationsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
    }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        currentLatLng = LatLng(location.latitude, location.longitude)
        cameraPositionState.move(CameraUpdateFactory.newLatLng(currentLatLng))
    }

    Scaffold(topBar = {
        TopBar(screen = Screens.MapScreen, navController = navController){
            navController.popBackStack()
        }
    }) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties
        ) {
            Marker(
                state = MarkerState(position = currentLatLng),
                title = "Current Location",
            )
        }
    }


}