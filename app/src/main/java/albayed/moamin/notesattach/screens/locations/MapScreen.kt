package albayed.moamin.notesattach.screens.locations

import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Location
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
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
import java.util.*

@SuppressLint("MissingPermission", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    noteId: String,
    viewModel: LocationsScreenViewModel = hiltViewModel()
) {
    val isOpenConfirmDialogue = remember {
        mutableStateOf(false)
    }
    val locationsCount = viewModel.locationsCount.collectAsState().value
    var locationChosen = remember {
        mutableStateOf(
            Location(
                noteId = UUID.fromString(noteId),
                latitude = 0.0,
                longitude = 0.0,
                description = ""
            )
        )
    }

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
    val geocoder = Geocoder(context, Locale.ENGLISH)

    Scaffold(topBar = {
        TopBar(screen = Screens.MapScreen) {
            navController.popBackStack()
        }
    }) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            onMapClick = {
                val details = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                Log.d("here", "MapScreen: ${details!![0].getAddressLine(0)}")
                locationChosen.value = Location(
                    noteId = UUID.fromString(noteId),
                    longitude = it.longitude,
                    latitude = it.latitude,
                    description = details[0].getAddressLine(0)
                )
                isOpenConfirmDialogue.value = true
            },
            onPOIClick = {
                Log.d("here", "MapScreen POI: ${it.name}")
                locationChosen.value = Location(
                    noteId = UUID.fromString(noteId),
                    longitude = it.latLng.longitude,
                    latitude = it.latLng.latitude,
                    description = it.name.replace("\n"," ")
                )
                isOpenConfirmDialogue.value = true
            }
        ) {
            Marker(
                state = MarkerState(position = currentLatLng),
                title = "Current Location",
            )
        }
    }

    if (isOpenConfirmDialogue.value) {
        ConfirmMessage(
            isOpenDialog = isOpenConfirmDialogue,
            onClickYes = {
                viewModel.createLocation(locationChosen.value)
                viewModel.updateLocationsCount(locationsCount = locationsCount + 1, noteId = noteId)
                navController.popBackStack()
            },
            onClickNo = { isOpenConfirmDialogue.value = false },
            title = "Add Location?",
            text = locationChosen.value.description
        )
    }


}