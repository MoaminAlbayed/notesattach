package albayed.moamin.notesattach.screens.noteEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.navigation.NavController
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditor(
    navController: NavController,
    isNewNote: Boolean,
    noteId: String?,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val titleFontSize = 18.sp
    val contentFontSize = 16.sp

    val keyboardController = LocalSoftwareKeyboardController.current


    var note = if (isNewNote) {
        Note(title = "", content = "")
    } else {
        produceState(initialValue = Note(title = "", content = "")) {
            value = noteId?.let { viewModel.getNoteById(it) }!!
        }.value
    }

    var title by rememberSaveable(note) {
        mutableStateOf(note.title)
    }
    var content by rememberSaveable(note) {
        mutableStateOf(note.content)
    }


    fun backToMainScreen() {
        if (isNewNote) {
            if (title.isEmpty() && content.isEmpty()) {
                navController.popBackStack()
            } else {
                note.title = title
                note.content = content
                viewModel.createNote(note)
                //navController.navigate(Screens.MainScreen.name)
                navController.popBackStack()
            }
        } else {
            note.title = title
            note.content = content
            viewModel.updateNote(note)
            //navController.navigate(Screens.MainScreen.name)
            navController.popBackStack()
        }
    }
    BackPressHandler() {
        backToMainScreen()
    }
    Scaffold(
        topBar = {
            TopBar(screen = Screens.NoteEditor, navController = navController) {
                backToMainScreen()
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
                    textStyle = TextStyle(fontSize = titleFontSize),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.textFieldColors(
                        //textColor = Color.Gray,
                        //disabledTextColor = Color.Transparent,
                        //backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        //disabledIndicatorColor = Color.Transparent
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    thickness = 1.dp,
                    color = Color.Black
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
                    textStyle = TextStyle(fontSize = contentFontSize),
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(onDone = {
//                        Log.d("keyboard", "NoteEditor: here")
//                        keyboardController?.hide()
//                    }),
                    colors = TextFieldDefaults.textFieldColors(
                        //textColor = Color.Gray,
                        //disabledTextColor = Color.Transparent,
                        //backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        //disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}


