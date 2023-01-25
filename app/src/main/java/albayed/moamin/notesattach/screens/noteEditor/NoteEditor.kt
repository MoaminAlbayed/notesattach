package albayed.moamin.notesattach.screens.noteEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.navigation.NavController
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditor(navController: NavController, isNewNote:Boolean, noteId:String?, viewModel: NoteEditorViewModel = hiltViewModel()) {
    val titleFontSize = 21.sp
    val contentFontSize = 19.sp


    var note = if (isNewNote) {Note(title = "", content = "") }
     else {
        produceState(initialValue = Note(title = "", content = "")) {
            value = noteId?.let { viewModel.getNoteById(it) }!!
        }.value
    }

    var title by remember (note){
        mutableStateOf(note.title)
    }
    var content by remember (note){
        mutableStateOf(note.content)
    }
    Scaffold(
        topBar = {
            TopBar(screen = Screens.NoteEditor, navController = navController){
                if (isNewNote) {
                    note.title=title
                    note.content=content
                    viewModel.createNote(note)
                } else {
                    note.title=title
                    note.content=content
                    viewModel.updateNote(note)
                }
                navController.navigate(Screens.MainScreen.name)
            }
        }
    ) {
        if (note == null)
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        else {
            Column() {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    maxLines = 1,
                    placeholder = {
                        Text(text = "Title", fontSize = titleFontSize)
                    },
                    textStyle = TextStyle(fontSize = titleFontSize)
                )
                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = content,
                    onValueChange = {
                        content = it
                    },
                    placeholder = {
                        Text(text = "Content", fontSize = contentFontSize)
                    },
                    textStyle = TextStyle(fontSize = contentFontSize)
                )
            }
        }
    }
}
