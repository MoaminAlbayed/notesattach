package albayed.moamin.notesattach.screens.mainscreen

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AttachmentIcon
import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {
    val notesList = viewModel.notesList.collectAsState().value

    val isSearch = remember { mutableStateOf(false) }
    //val searchState = remember { mutableStateOf("") }

    val notesToShow = remember { mutableStateOf(listOf<Note>()) }

    if (!isSearch.value) {
        Log.d("here", "MainScreen: on if")
        notesToShow.value = notesList
    }
//        if (searchState.value.isEmpty())
////            notesToShow.value = emptyList()
//            notesToShow.value = notesList.filter {note ->
//                Log.d("here", "MainScreen: ${note.title}")
//                note.title.contains(searchState.value) /*|| note.content.contains(searchState.value)*/
//        }
//    } else
//        notesToShow.value = notesList

    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.MainScreen,
                isMainScreenSearch = isSearch,
                firstAction = { isSearch.value = !isSearch.value },
                onSearchValueChanged = { value ->
                    notesToShow.value = notesList.filter { note ->
                        note.title.contains(value) || note.content.contains(value)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.add, contentDescription = "New Note Button") {
                navController.navigate(Screens.NoteEditor.name + "/${true}/${false}/${null}")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp)
        ) {
//            items(notesList.asReversed()) { note ->
            items(notesToShow.value.asReversed()) { note ->
                NoteCard(note, navController = navController) {
                    navController.navigate(Screens.NoteEditor.name + "/${false}/${false}/${note.id}")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "NoteCard Dark Theme"
//)
@Composable
fun NoteCard(
    note: Note,
    viewModel: MainScreenViewModel = hiltViewModel(),
    navController: NavController,
    onClick: () -> Unit
) {

    val context = LocalContext.current

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
    var route by remember { mutableStateOf("") }
    val getPermissions = requestMyPermissions(route, context, navController)

    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }
    val attachmentsColumnsWidth = 70.dp
    val attachmentIconScale = 2.2f
    val attachmentIconPadding = 5.dp



    Card(
        modifier = Modifier
            .padding(5.dp)
            .height(180.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        // .clickable { onClick.invoke() },
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
        elevation = 5.dp

    ) {
        Row() {
            Column(
                modifier = Modifier.width(attachmentsColumnsWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier = Modifier
                    .clickable {
                        navController.navigate(Screens.ImagesScreen.name + "/${note.id}")
                    }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.photo,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Photo Button",
                        tint = MaterialTheme.colors.primary,
                        count = note.imagesCount
                    )
                }
                Box(modifier = Modifier
                    .clickable {
                        navController.navigate(Screens.VideosScreen.name + "/${note.id}")
                    }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.video,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Video Button",
                        tint = MaterialTheme.colors.primary,
                        count = note.videosCount
                    )
                }
                Box(modifier = Modifier
                    .clickable {
                        if (checkPermissions(recordPermission, context))
                            navController.navigate(Screens.AudioClipsScreen.name + "/${note.id}")
                        else {
                            route = Screens.AudioClipsScreen.name + "/${note.id}"
                            getPermissions.launch(recordPermission)
                        }
                    }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.mic,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Microphone Button",
                        tint = MaterialTheme.colors.primary,
                        count = note.audioClipsCount
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.primary

            )

            Column(modifier = Modifier
                .weight(1f)
                .clickable { onClick.invoke() }) {
                if (note.title.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                        text = note.title,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Text(
                    modifier = Modifier.padding(
                        start = 5.dp,
                        bottom = 2.dp,
                        top = if (note.title.isNotEmpty()) 0.dp else 2.dp
                    ),
                    text = "Created: ${dateFormatter(note.date.time)}",
                    style = TextStyle(fontWeight = FontWeight.Thin, fontSize = 14.sp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colors.primary
                )
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(),
                    text = note.content,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Divider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.primary
            )

            Column(
                modifier = Modifier.width(attachmentsColumnsWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier
                    .clickable {
                        if (internetAvailable(context))
                            if (checkPermissions(locationPermissions, context))
                                navController.navigate(Screens.LocationsScreen.name + "/${note.id}")
                            else {
                                route = Screens.LocationsScreen.name + "/${note.id}"
                                getPermissions.launch(locationPermissions)
                            }
                        else
                            Toast
                                .makeText(
                                    context,
                                    "Internet connection is required to use Locations",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                    }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.location,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Locations Button",
                        tint = MaterialTheme.colors.primary,
                        count = note.locationsCount
                    )
                }
                Box(modifier = Modifier
                    .clickable {
                        if (checkPermissions(alarmPermissions, context))
                            navController.navigate(Screens.AlarmsScreen.name + "/${note.id}/?${note.title}")
                        else {
                            route = Screens.AlarmsScreen.name + "/${note.id}/?${note.title}"
                            getPermissions.launch(alarmPermissions)
                        }
                    }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.alarm,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Alarms Button",
                        tint = MaterialTheme.colors.primary,
                        count = note.alarmsCount
                    )
                }
                Box(modifier = Modifier
                    .clickable { isOpenDeleteDialog.value = true }
                    .fillMaxSize()
                    .weight(1f)
                ) {
                    AttachmentIcon(
                        icon = R.drawable.trash,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Delete Button",
                        tint = Color.Red.copy(alpha = 0.7f),
                        isDelete = true
                    )
                }
            }
        }
    }
    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                viewModel.deleteNote(note)
                isOpenDeleteDialog.value = false
            },
            onClickNo = { isOpenDeleteDialog.value = false },
            title = "Deleting Note",
            text = "Are you sure you want to delete this note and all of its attachments?",
        )
    }
}

