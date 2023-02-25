package albayed.moamin.notesattach.screens.locations

import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Location
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationRequest
import android.util.Log
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

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
    }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, object : CancellationToken() {
        override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

        override fun isCancellationRequested() = false
    })
        .addOnSuccessListener {
            if (it == null)
                Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {

                currentLatLng = LatLng(it.latitude, it.longitude)
                cameraPositionState.move(CameraUpdateFactory.newLatLng(currentLatLng))
            }

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
                geocoder.getFromLocation(
                    it.latitude,
                    it.longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            Log.d("here", "onGeocode: $addresses")
                            locationChosen.value = Location(
                                noteId = UUID.fromString(noteId),
                                longitude = it.longitude,
                                latitude = it.latitude,
                                description = addresses[0].getAddressLine(0)
                            )

                            isOpenConfirmDialogue.value = true
                        }
                        override fun onError(errorMessage: String?) {
                            super.onError(errorMessage)
                            Log.d("here", "onError: $errorMessage")
                            locationChosen.value = Location(
                                noteId = UUID.fromString(noteId),
                                longitude = it.longitude,
                                latitude = it.latitude,
                                description = "Latitude: ${it.latitude}\nLongitude: ${it.longitude}"
                            )
                            isOpenConfirmDialogue.value = true
                        }
                    })
            },
            onPOIClick = {
                Log.d("here", "MapScreen POI: ${it.name}")
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
                state = MarkerState(position = currentLatLng),
                title = "Current Location",
                onClick = {
                    geocoder.getFromLocation(
                        currentLatLng.latitude,
                        currentLatLng.longitude,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                Log.d("here", "onGeocode: $addresses")
                                locationChosen.value = Location(
                                    noteId = UUID.fromString(noteId),
                                    longitude = currentLatLng.longitude,
                                    latitude = currentLatLng.latitude,
                                    description = addresses[0].getAddressLine(0)
                                )

                                isOpenConfirmDialogue.value = true
                            }
                            override fun onError(errorMessage: String?) {
                                super.onError(errorMessage)
                                Log.d("here", "onError: $errorMessage")
                                locationChosen.value = Location(
                                    noteId = UUID.fromString(noteId),
                                    longitude = currentLatLng.longitude,
                                    latitude = currentLatLng.latitude,
                                    description = "Latitude: ${currentLatLng.latitude}\nLongitude: ${currentLatLng.longitude}"
                                )
                                isOpenConfirmDialogue.value = true
                            }
                        })
                    return@Marker true
                }
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