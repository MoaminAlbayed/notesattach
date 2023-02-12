package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import albayed.moamin.notesattach.R


@Composable
fun TopBar(
    screen: Screens,
    navController: NavController,
    firstAction: () -> Unit = {},
    secondAction: () -> Unit = {},
    thirdAction: ()-> Unit ={},
    onClick: () -> Unit = {}
) {
    when (screen) {//todo refactor to make shorter
        Screens.MainScreen -> {
            TopAppBar(title = {
                Text(text = "My Notes")
            },
                backgroundColor = Color.Black,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Button"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
                        )
                    }
                }
            )
        }
        Screens.NoteEditor -> {
            TopAppBar(title = {
                Text(text = "Note Editor")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }
            )
        }
        Screens.ImagesScreen -> {
            TopAppBar(title = {
                Text(text = "Images Viewer")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { firstAction.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete Button"
                        )
                    }
                }
            )
        }
        Screens.VideosScreen -> {
            TopAppBar(title = {
                Text(text = "Videos Viewer")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { firstAction.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete Button"
                        )
                    }
                }
            )
        }
        Screens.AudioClipsScreen -> {
            TopAppBar(title = {
                Text(text = "Audio Clips Viewer")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { firstAction.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete Button"
                        )
                    }
                }
            )
        }
        Screens.RecordAudioScreen -> {
            TopAppBar(title = {
                Text(text = "Record a Clip")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
            )
        }
        Screens.LocationsScreen -> {
            TopAppBar(title = {
                Text(text = "Locations List")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { firstAction.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete Button"
                        )
                    }
                }
            )
        }
        Screens.MapScreen -> {
            TopAppBar(title = {
                Text(text = "Map Viewer")
            },
                navigationIcon = {
                    IconButton(onClick = { onClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
            )
        }
        else -> {}
    }

}