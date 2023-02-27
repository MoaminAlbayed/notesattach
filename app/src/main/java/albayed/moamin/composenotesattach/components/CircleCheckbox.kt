package albayed.moamin.composenotesattach.components

import albayed.moamin.composenotesattach.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CircleCheckbox(modifier: Modifier = Modifier, selected: Boolean, enabled: Boolean = true, onChecked: () -> Unit) {

    val imageVector = if (selected) R.drawable.check_circle else R.drawable.outline_circle
    val tint = if (selected) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (selected) Color.White else Color.Transparent

    IconButton(
        modifier = modifier,
        onClick = { onChecked() },
        enabled = enabled
    ) {

        Icon(
            painter = painterResource(id = imageVector), tint = tint,
            modifier = Modifier.background(color = background, shape = CircleShape),
            contentDescription = "checkbox"
        )
    }
}