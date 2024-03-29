package albayed.moamin.composenotesattach.screens.noteEditor

import albayed.moamin.composenotesattach.components.AttachmentsDropDown
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.navigation.NavController
import albayed.moamin.composenotesattach.components.TopBar
import albayed.moamin.composenotesattach.models.Note
import albayed.moamin.composenotesattach.navigation.Screens
import albayed.moamin.composenotesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay


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
    val focusRequester = remember { FocusRequester() }

    //show keyboard and focus on title if creating a new note
    if (isNewNote) {
        LaunchedEffect(key1 = Unit) {
            delay(100)
            focusRequester.requestFocus()
        }
    }

    val titleFontSize = 18.sp
    val contentFontSize = 16.sp

    //if existing note populate title and content from note else keep them empty
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
    
    var isNoteCreated by rememberSaveable() {
        mutableStateOf(!isNewNote)
    }

    val isAttachmentsDropDownVisible = remember { mutableStateOf(false) }
    if (isAttachmentsDropDownVisible.value && !isNewNote) {
        note.title = title
        note.content = content
        viewModel.updateNote(note)
        AttachmentsDropDown(
            isVisible = isAttachmentsDropDownVisible,
            note = note,
            navController = navController,
            context = context
        )
    }


    fun backToMainScreen() {
        if (isFromNotification)
            navController.navigateUp()
        else
            navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }
    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.NoteEditor,
                isNewNote = isNewNote,
                firstAction = { isAttachmentsDropDownVisible.value = true }
            ) {
                backToMainScreen()
            }
        }
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = title,
                onValueChange = {
                    title = it
                    if (it.isEmpty() && note.content.isEmpty()){
                        viewModel.deleteNote(note)
                        isNoteCreated = false
                    }
                    else {
                        note.title = it
                        if (!isNoteCreated) {
                            viewModel.createNote(note)
                            isNoteCreated = true
                        }
                        else {
                            viewModel.updateNote(note)
                        }
                    }
                },
                maxLines = 1,
                placeholder = {
                    Text(text = "Title", fontSize = titleFontSize)
                },
                textStyle = TextStyle(fontSize = titleFontSize),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    cursorColor = MaterialTheme.colors.onSurface,
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
                    if (it.isEmpty() && note.title.isEmpty()){
                        viewModel.deleteNote(note)
                        isNoteCreated = false
                    }
                    else {
                        note.content = it
                        if (!isNoteCreated) {
                            viewModel.createNote(note)
                            isNoteCreated = true
                        }
                        else {
                            viewModel.updateNote(note)

                        }
                    }
                },
                placeholder = {
                    Text(text = "Content", fontSize = contentFontSize)
                },
                textStyle = TextStyle(fontSize = contentFontSize),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}


