package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController

@Composable
fun FloatingButton(screen: Screens, navController: NavController) {
    when (screen){
        Screens.MainScreen -> {
            var offsetX = remember{ mutableStateOf(0) }
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value, 0) }
                    .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState(){
                        offsetX.value += it.toInt()
                    }
                ),
                onClick = { navController.navigate(Screens.NoteEditor.name+"/${true}/${null}") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create New Note")
            }
        }
        Screens.NoteEditor -> {
            //TODO
        }
    }
}