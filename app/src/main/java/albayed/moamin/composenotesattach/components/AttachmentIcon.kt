package albayed.moamin.composenotesattach.components


import albayed.moamin.composenotesattach.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AttachmentIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.photo,
    scale: Float = 2.1f,
    padding: Dp = 5.dp,
    contentDescription: String = "",
    tint: Color = Color.Unspecified,
    count: Int = 1,
    isDelete: Boolean = false,
) {
    Box(//box inside a box can be used to position the inner box in a specific location in the outer box
        modifier = modifier.fillMaxSize().padding(padding),
    ) {

        Icon(
            modifier = modifier
                .scale(scale).align(Alignment.Center),
            painter = painterResource(id = icon),
            tint = tint,
            contentDescription = contentDescription
        )
        if (!isDelete && count != 0) {//isDelete is used to know if the button is a note delete button
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Surface(
                    modifier = Modifier.align(Alignment.Center),
                    shape = CircleShape,
                    color = MaterialTheme.colors.onSurface,
                    contentColor = MaterialTheme.colors.surface
                ) {
                    val countString = if (count < 10) count.toString() else "+9"
                    Text(text = " $countString ", fontSize = 15.sp)
                }

            }
        }

    }
}

