package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CircleCheckbox(selected: Boolean, enabled: Boolean = true, onChecked: () -> Unit) {

    //val color = MaterialTheme.colors.primary
    val imageVector = if (selected) R.drawable.check_circle else R.drawable.outline_circle
    val tint = if (selected) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (selected) Color.White else Color.Transparent
//    val background = Color.Transparent

    IconButton(
        onClick = { onChecked() },
        // modifier = Modifier.offset(x = 4.dp, y = 4.dp),
        enabled = enabled
    ) {

        Icon(
            painter = painterResource(id = imageVector), tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox"
        )
    }
}