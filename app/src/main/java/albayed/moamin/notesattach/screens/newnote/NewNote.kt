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
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewNote(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(screen = Screens.NewNote, navController = navController)
        }
    ) {
        NewNoteForm()
    }

}

@Composable
fun NewNoteForm() {
    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }
    Column() {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {
                title = it
            },
            maxLines = 1,
            label = {
                Text(text = "Title", fontSize = 21.sp)
            }
        )
        TextField(
            modifier = Modifier.fillMaxSize(),
            value = content,
            onValueChange = {
                content = it
            },
            label = {
                Text(text = "Content")
            },
            textStyle = TextStyle(fontSize = 19.sp)
        )
    }
}