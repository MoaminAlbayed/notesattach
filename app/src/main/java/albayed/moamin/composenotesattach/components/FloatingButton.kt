package albayed.moamin.composenotesattach.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.res.painterResource

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String,
    action: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val offsetY by remember { mutableStateOf(0f) }

    FloatingActionButton(modifier = modifier
        .offset {//offset is changed by dragging
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