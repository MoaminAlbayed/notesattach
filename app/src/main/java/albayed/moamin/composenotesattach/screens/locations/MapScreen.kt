package albayed.moamin.composenotesattach.screens.locations

import albayed.moamin.composenotesattach.components.ConfirmMessage
import albayed.moamin.composenotesattach.components.TopBar
import albayed.moamin.composenotesattach.models.Location
import albayed.moamin.composenotesattach.navigation.Screens
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationRequest
import android.widget.Toast
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
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.compose.*
import java.util.*

@SuppressLint("MissingPermission", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    noteId: String,
    viewModel: LocationsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isOpenConfirmDialogue = remember {
        mutableStateOf(false)
    }
    val locationsCount = viewModel.locationsCount.collectAsState().value
    val locationChosen = remember {
        mutableStateOf(
            Location(
                noteId = UUID.fromString(noteId),
                latitude = 0.0,
                longitude = 0.0,
                description = ""
            )
        )
    }

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val markerState = rememberMarkerState(null, currentLatLng)


    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
    }

    //fusedLocationClient used to get current location
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.getCurrentLocation(
        LocationRequest.QUALITY_HIGH_ACCURACY,
        object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
        .addOnSuccessListener {
            if (it == null)
                Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                currentLatLng = LatLng(it.latitude, it.longitude)
                cameraPositionState.move(CameraUpdateFactory.newLatLng(currentLatLng))
                //markerState position needs to be changed manually otherwise it'll stay on 0,0
                markerState.position = currentLatLng
            }
        }

    val geocoder = Geocoder(context, Locale.ENGLISH)
    fun getLocationFromGeocoder(location: LatLng) {
        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    locationChosen.value = Location(
                        noteId = UUID.fromString(noteId),
                        longitude = location.longitude,
                        latitude = location.latitude,
                        description = addresses[0].getAddressLine(0)
                    )

                    isOpenConfirmDialogue.value = true
                }

                //onError used in case geocoder fails to fetch location information, uses lat and long instead
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    locationChosen.value = Location(
                        noteId = UUID.fromString(noteId),
                        longitude = location.longitude,
                        latitude = location.latitude,
                        description = "Latitude: ${location.latitude}\nLongitude: ${location.longitude}"
                    )
                    isOpenConfirmDialogue.value = true
                }
            })
    }

    Scaffold(topBar = {
        TopBar(screen = Screens.MapScreen) {
            navController.popBackStack()
        }
    }) {
        LaunchedEffect(key1 = Unit) {
            Toast.makeText(context, "Tap on any location to add", Toast.LENGTH_LONG).show()
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            onMapClick = {
                getLocationFromGeocoder(it)
            },
            onPOIClick = {
                locationChosen.value = Location(
                    noteId = UUID.fromString(noteId),
                    longitude = it.latLng.longitude,
                    latitude = it.latLng.latitude,
                    description = it.name.replace("\n", " ")
                )
                isOpenConfirmDialogue.value = true
            }
        ) {
            Marker(
                state = markerState,
                title = "Current Location",
                onClick = {
                    getLocationFromGeocoder(currentLatLng)
                    return@Marker true
                }
            )
            markerState.showInfoWindow()
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