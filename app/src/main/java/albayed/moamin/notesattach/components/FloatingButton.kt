package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.annotation.DrawableRes
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
import albayed.moamin.notesattach.R
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String,
    action: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    FloatingActionButton(modifier = modifier
        .offset {
            IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
        }
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState() {
                offsetX += it.toInt()
            }
        ),
        onClick = { action.invoke() },
        backgroundColor = MaterialTheme.colors.onPrimary,
        contentColor = MaterialTheme.colors.primary,
        shape = CircleShape
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = contentDescription)
    }
}