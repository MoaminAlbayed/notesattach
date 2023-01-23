package albayed.moamin.notesattach.screens

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AttachmentIcon
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(screen = Screens.MainScreen, navController = navController) },
        floatingActionButton = {
            FloatingButton(
                screen = Screens.MainScreen,
                navController = navController
            )
        }
    ) {
        NoteCard()
    }

}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "NoteCard Dark Theme"
)
@Composable
fun NoteCard(
    title: String = "Title",
    dateTate: String = "01-01-2023 12:05",
    content: String = "Content"
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .height(180.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
        elevation = 5.dp

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
                    .width(2.dp)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.primary

            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp, bottom = 2.dp),
                    text = "Created: $dateTate",
                    style = TextStyle(fontWeight = FontWeight.Thin, fontSize = 13.sp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colors.primary
                )
                Text(modifier = Modifier.padding(5.dp), text = content, fontSize = 17.sp)
            }

            Divider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(0.9f)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.primary
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

