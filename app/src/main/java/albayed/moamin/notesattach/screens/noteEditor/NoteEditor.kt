package albayed.moamin.notesattach.screens.noteEditor

import albayed.moamin.notesattach.components.AttachmentsDropDown
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.navigation.NavController
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteEditor(
    navController: NavController,
    isNewNote: Boolean,
    isFromNotification: Boolean,
    noteId: String?,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val titleFontSize = 18.sp
    val contentFontSize = 16.sp

    val note = if (isNewNote) {
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

    val isAttachmentsDropDownVisible = remember { mutableStateOf(false) }
    if (isAttachmentsDropDownVisible.value && !isNewNote){
        AttachmentsDropDown(
            isVisible = isAttachmentsDropDownVisible,
            note = note,
            navController = navController,
            context = context
        )
    }


    fun backToMainScreen() {
        if (isNewNote) {
            if (title.isEmpty() && content.isEmpty()) {
                navController.popBackStack()
            } else {
                note.title = title
                note.content = content
                viewModel.createNote(note)
                navController.popBackStack()
            }
        } else {
            note.title = title
            note.content = content
            viewModel.updateNote(note)
            if (isFromNotification)
                navController.navigateUp()
            else
                navController.popBackStack()
        }
    }
    BackPressHandler() {
        backToMainScreen()
    }
    Scaffold(
        topBar = {//todo add access to attachments from note editor screen
            TopBar(
                screen = Screens.NoteEditor,
                isNewNote = isNewNote,
                firstAction = {isAttachmentsDropDownVisible.value = true}
            ) {
                backToMainScreen()
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
                    textStyle = TextStyle(fontSize = titleFontSize),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
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
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
    }
}


