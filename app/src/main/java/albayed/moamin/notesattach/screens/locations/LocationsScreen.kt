package albayed.moamin.notesattach.screens.locations

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.LocationCard
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Location
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.*
import com.google.maps.android.compose.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationsScreen(
    navController: NavController,
    noteId: String,
    viewModel: LocationsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationsList = viewModel.location.collectAsState().value
    val locationsCount = viewModel.locationsCount.collectAsState().value
    val isDeleteMode = remember {
        mutableStateOf(false)
    }
    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }
    val isOpenNavDialog = remember {
        mutableStateOf(false)
    }
    val locationToNavigate = remember {
        mutableStateOf<Location?>(null)
    }
    val locationsToDelete = remember {
        mutableStateListOf<Location>()
    }

    val navigationLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        }

    fun navigationUri(location: Location): String {
        return "google.navigation:q=${location.latitude},${location.longitude}"
    }

    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.LocationsScreen,
                navController = navController,
                firstAction = {
                    if (locationsCount == 0) {
                        Toast.makeText(context, "No Locations to Delete!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!isDeleteMode.value) {
                        isDeleteMode.value = true
                    } else {
                        if (locationsToDelete.isNotEmpty()) {
                            isOpenDeleteDialog.value = true
                        } else {
                            isDeleteMode.value = false
                        }
                    }
                }
            ) {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                    locationsToDelete.clear()
                } else {
                    navController.popBackStack()
                }
            }
        },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.location, contentDescription = "Add Location Button") {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                }
                navController.navigate(Screens.MapScreen.name + "/$noteId")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(locationsList.asReversed()) { location ->
                LocationCard(
                    location = location,
                    isDeleteMode = isDeleteMode,
                    isNewDeleteProcess = locationsToDelete.isEmpty(),
                    onClick = { location ->
                        locationToNavigate.value = location
                        isOpenNavDialog.value = true
                    },
                    checkedDelete = { checkedDelete ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            locationsToDelete.remove(location)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            locationsToDelete.add(location)
                        }
                    }
                )
            }
        }
    }
    if (isOpenNavDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenNavDialog,
            onClickYes = {
                navigationLauncher.launch(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(navigationUri(locationToNavigate.value!!)))
                )
                isOpenNavDialog.value = false
            },
            onClickNo = { isOpenNavDialog.value = false },
            title = "Navigation",
            text = "Start navigation to selected location?"
        )
    }

    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                locationsToDelete.forEach { location ->
                    viewModel.deleteLocation(location)
                }
                viewModel.updateLocationsCount(
                    locationsCount = locationsCount - locationsToDelete.size,
                    noteId = noteId
                )
                locationsToDelete.clear()
                isOpenDeleteDialog.value = false
                isDeleteMode.value = false
            },
            onClickNo = {
                isOpenDeleteDialog.value = false
                locationsToDelete.clear()
                isDeleteMode.value = false
            },
            title = "Deleting Locations",
            text = "Are you sure you want to delete ${locationsToDelete.size} location(s)?"
        )
    }
    fun backToMainScreen() {
        if (isDeleteMode.value) {
            isDeleteMode.value = false
            locationsToDelete.clear()
        } else
            navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }


}