package albayed.moamin.notesattach.components


import albayed.moamin.notesattach.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun AttachmentIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.photo,
    contentDescription: String = "",
    tint: Color = Color.Unspecified,
    count: Int = 0,
    isDelete: Boolean = false,
    onClick: () -> Unit = {}
) {
    ConstraintLayout(modifier = Modifier.padding(5.dp)) {
        //val (iconButton, counter) = createRefs()
        val counterRef = createRef()
        IconButton(modifier = modifier
            .scale(2.2f),
//            .constrainAs(iconButton) {
//            },
            onClick = { onClick.invoke() }
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = tint,
                contentDescription = contentDescription
            )
        }
        if (!isDelete && count != 0) {
            Counter(
                modifier = Modifier.constrainAs(counterRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                count = count,
                onClick = onClick
            )
        }
    }
}

@Composable
fun Counter(modifier: Modifier = Modifier, count: Int ,onClick: () -> Unit) {
    Row(
        modifier = modifier.clickable { onClick.invoke() },
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colors.primary, contentColor = MaterialTheme.colors.onPrimary) {
            val countString = if (count < 10) count.toString() else "+9"
            Text(text = " $countString ")
        }

    }
}

