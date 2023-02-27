package albayed.moamin.composenotesattach.components

import albayed.moamin.composenotesattach.R
import albayed.moamin.composenotesattach.models.Note
import albayed.moamin.composenotesattach.navigation.Screens
import albayed.moamin.composenotesattach.utils.checkPermissions
import albayed.moamin.composenotesattach.utils.internetAvailable
import albayed.moamin.composenotesattach.utils.requestMyPermissions
import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AttachmentsDropDown(
    isVisible: MutableState<Boolean>,
    note: Note,
    navController: NavController,
    context: Context
) {
    val attachmentIconScale = 2.2f
    val attachmentIconPadding = 2.dp

    var route by remember { mutableStateOf("") }
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val alarmPermissions = arrayOf(
        Manifest.permission.SET_ALARM,
        Manifest.permission.POST_NOTIFICATIONS
    )
    val recordPermission = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    val getPermissions = requestMyPermissions(route, context, navController)
    val attachmentIconColor = MaterialTheme.colors.onSurface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = isVisible.value,
            onDismissRequest = { isVisible.value = false }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                DropdownMenuItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        isVisible.value = false
                        navController.navigate(Screens.ImagesScreen.name + "/${note.id}")
                    }) {
                    AttachmentIcon(
                        icon = R.drawable.photo,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Camera Button",
                        tint = attachmentIconColor,
                        count = note.imagesCount
                    )
                }
                DropdownMenuItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        isVisible.value = false
                        if (internetAvailable(context)) {
                            if (checkPermissions(locationPermissions, context))
                                navController.navigate(Screens.LocationsScreen.name + "/${note.id}")
                            else {
                                route = Screens.LocationsScreen.name + "/${note.id}"
                                getPermissions.launch(locationPermissions)
                            }
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Internet connection is required to use Locations",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }) {
                    AttachmentIcon(
                        icon = R.drawable.location,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Locations Button",
                        tint = attachmentIconColor,
                        count = note.locationsCount
                    )
                }
            }

            Row(modifier = Modifier.fillMaxSize()) {
                DropdownMenuItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        isVisible.value = false
                        navController.navigate(Screens.VideosScreen.name + "/${note.id}")
                    }) {
                    AttachmentIcon(
                        icon = R.drawable.video,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Video Button",
                        tint = attachmentIconColor,
                        count = note.videosCount
                    )

                }
                DropdownMenuItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        isVisible.value = false
                        if (checkPermissions(alarmPermissions, context))
                            navController.navigate(Screens.AlarmsScreen.name + "/${note.id}/?${note.title}")
                        else {
                            route = Screens.AlarmsScreen.name + "/${note.id}/?${note.title}"
                            getPermissions.launch(alarmPermissions)
                        }
                    }) {
                    AttachmentIcon(
                        icon = R.drawable.alarm,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Alarms Button",
                        tint = attachmentIconColor,
                        count = note.alarmsCount
                    )
                }
            }
            Row(modifier = Modifier.fillMaxSize(0.5f)) {
                DropdownMenuItem(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        isVisible.value = false
                        if (checkPermissions(recordPermission, context))
                            navController.navigate(Screens.AudioClipsScreen.name + "/${note.id}")
                        else {
                            route = Screens.AudioClipsScreen.name + "/${note.id}"
                            getPermissions.launch(recordPermission)
                        }
                    }) {
                    AttachmentIcon(
                        icon = R.drawable.mic,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Mic Button",
                        tint = attachmentIconColor,
                        count = note.audioClipsCount
                    )
                }
            }
        }
    }
}
