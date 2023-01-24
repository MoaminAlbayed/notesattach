package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


@Composable
fun TopBar(screen: Screens, navController: NavController, onClick: () -> Unit = {}) {
    when (screen) {
        Screens.MainScreen -> {
            TopAppBar(title = {
                Text(text = "My Notes")
            },
                backgroundColor = Color.Black,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Button")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                }
            )
        }
        Screens.NewNote ->{
            TopAppBar(title ={
                Text(text = "Note Editor")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                    }
                }
            )
        }
        //else -> {}
    }

}