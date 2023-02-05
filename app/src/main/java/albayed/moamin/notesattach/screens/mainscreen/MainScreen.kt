package albayed.moamin.notesattach.screens

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AttachmentIcon
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.screens.mainscreen.MainScreenViewModel
import albayed.moamin.notesattach.utils.dateFormatter
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {
    val notesList = viewModel.notesList.collectAsState().value

    Scaffold(
        topBar = { TopBar(screen = Screens.MainScreen, navController = navController) },
        floatingActionButton = {
//            FloatingButton(
//                screen = Screens.MainScreen,
//                navController = navController
//            )
            FloatingButton(icon = R.drawable.add, contentDescription = "New Note Button") {
                navController.navigate(Screens.NoteEditor.name + "/${true}/${null}")
            }
        }
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, end = 5.dp)) {
            items(notesList.asReversed()) { note ->
                NoteCard(note, navController = navController) {
                    navController.navigate(Screens.NoteEditor.name + "/${false}/${note.id}")
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
    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }
    val attachmentsColumnsWeight: Float = 0.25f
    Card(
        modifier = Modifier
            .padding(5.dp)
            .height(180.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
        elevation = 5.dp

    ) {
        Row() {
            Column(
                modifier = Modifier.weight(attachmentsColumnsWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                AttachmentIcon(
                    icon = R.drawable.photo,
                    contentDescription = "Photo Button",
                    tint = MaterialTheme.colors.primary,
                    count = note.imagesCount
                ) {
                    navController.navigate(Screens.ImagesScreen.name + "/${note.id}")
                }
                AttachmentIcon(
                    icon = R.drawable.video,
                    contentDescription = "Video Button",
                    tint = MaterialTheme.colors.primary,
                    count = note.videosCount
                )
                AttachmentIcon(
                    icon = R.drawable.mic,
                    contentDescription = "Microphone Button",
                    tint = MaterialTheme.colors.primary,
                    count = note.voiceClipsCount
                )
            }

            Divider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.primary

            )

            Column(modifier = Modifier.weight(1f)) {
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
                    modifier = Modifier.padding(start = 5.dp, bottom = 2.dp, top = if (note.title.isNotEmpty()) 0.dp else 2.dp),
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
                    modifier = Modifier.padding(5.dp),
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
                modifier = Modifier.weight(attachmentsColumnsWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AttachmentIcon(
                    icon = R.drawable.location,
                    contentDescription = "Locations Button",
                    tint = MaterialTheme.colors.primary,
                    count = note.locationsCount
                )
                AttachmentIcon(
                    icon = R.drawable.alarm,
                    contentDescription = "Alarms Button",
                    tint = MaterialTheme.colors.primary,
                    count = note.alarmsCount
                )
                AttachmentIcon(
                    icon = R.drawable.trash,
                    contentDescription = "Delete Button",
                    tint = Color.Red.copy(alpha = 0.7f),
                    isDelete = true
                ) {
                    isOpenDeleteDialog.value = true

                }
            }
        }
    }
    if (isOpenDeleteDialog.value) {
        AlertDialog(onDismissRequest = { isOpenDeleteDialog.value = false },
            buttons = {
                TextButton(onClick = {
                    viewModel.deleteNote(note)//todo delete all attachments
                    isOpenDeleteDialog.value = false
                }) {
                    Text(text = "Yes", style = MaterialTheme.typography.button)
                }
                TextButton(onClick = { isOpenDeleteDialog.value = false }) {
                    Text(text = "No", style = MaterialTheme.typography.button)
                }
            },
            title = {
                Text(text = "Deleting Note", style = MaterialTheme.typography.h6)
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this note and all of its attachments?",
                    style = MaterialTheme.typography.body1
                )
            }
        )
    }
}

