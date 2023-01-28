package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import kotlin.math.roundToInt

@Composable
fun FloatingButton(screen: Screens, navController: NavController) = when (screen) {
    Screens.MainScreen -> {

       // BoxWithConstraints() {//this is needed for blocking dragging otuside of screen when using pointerInput
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
         //   val parentWidth = constraints.maxWidth
         //   val parentHeight = constraints.maxHeight
            FloatingActionButton(
                modifier = Modifier
                    .offset {
                        IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                    }
//                    .pointerInput(Unit) {
//                        val boxSize = this.size
//                        detectDragGestures { change, dragAmount ->
//                            change.consume()
////                            offsetX += dragAmount.x
////                            offsetY += dragAmount.y
//                            offsetX = (offsetX + dragAmount.x).coerceIn(
//                                boxSize.width.toFloat() -parentWidth,
//                                0f
//                            )
//                            offsetY = (offsetY + dragAmount.y).coerceIn(
//                                boxSize.height.toFloat() -parentHeight,
//                                0f
//                            )
//                        }
//                    },
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState() {
                        offsetX += it.toInt()
                    }
                ),
                onClick = { navController.navigate(Screens.NoteEditor.name + "/${true}/${null}") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create New Note")
            }
        }

  //  }
    else -> {
        //TODO
    }
}