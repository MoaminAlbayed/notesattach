package albayed.moamin.notesattach.screens.newnote

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewNote(navController: NavController, viewModel: NewNoteViewModel = hiltViewModel()) {
    val titleFontSize = 21.sp
    val contentFontSize = 19.sp
    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            TopBar(screen = Screens.NewNote, navController = navController){
                viewModel.createNote(Note(title = title, content = content))
                navController.navigate(Screens.MainScreen.name)
            }
        }
    ) {
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
