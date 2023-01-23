package albayed.moamin.notesattach.screens

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AttachmentIcon
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold() {
        NoteCard()
    }

}

@Preview()
@Composable
fun NoteCard(title: String = "Title", content: String = "Content") {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .height(180.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, color = Color.Gray),

        ) {
        Row() {
            Column(
                // modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AttachmentIcon(
                    icon = R.drawable.photo,
                    contentDescription = "Photo Button",
                    tint = MaterialTheme.colors.primary
                )
                AttachmentIcon(
                    icon = R.drawable.video,
                    contentDescription = "Video Button",
                    tint = MaterialTheme.colors.primary
                )
                AttachmentIcon(
                    icon = R.drawable.mic,
                    contentDescription = "Microphone Button",
                    tint = MaterialTheme.colors.primary
                )
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(modifier = Modifier.padding(5.dp), text = content, fontSize = 17.sp)
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )

            Column(
                //modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AttachmentIcon(
                    icon = R.drawable.location,
                    contentDescription = "Locations Button",
                    tint = MaterialTheme.colors.primary
                )
                AttachmentIcon(
                    icon = R.drawable.alarm,
                    contentDescription = "Alarms Button",
                    tint = MaterialTheme.colors.primary
                )
                AttachmentIcon(
                    icon = R.drawable.delete,
                    contentDescription = "Delete Button",
                    tint = Color.Red,
                    isDelete = true
                )

            }
        }
    }
}