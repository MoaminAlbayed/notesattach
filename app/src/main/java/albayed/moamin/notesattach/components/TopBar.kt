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
import albayed.moamin.notesattach.R


@Composable
fun TopBar(
    screen: Screens,
    firstAction: () -> Unit = {},
    isNewNote: Boolean = false,
    onClick: () -> Unit = {}
) {
    when (screen) {//todo refactor to make shorter
        Screens.MainScreen -> {
            TopAppBar(title = {
                Text(text = "My Notes")
            },
                backgroundColor = Color.Black,
                actions = {
                    IconButton(onClick = { /*TODO implement search in notes*/ }) {
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
            if (!isNewNote){
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
                    },
                    actions = {
                        IconButton(onClick = { firstAction.invoke() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.attachment_button),
                                contentDescription = "Attachments Button"
                            )
                        }
                    }
                )
            } else
            TopAppBar(title = {
                Text(text = "New Note")
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
        Screens.AlarmsScreen -> {
            TopAppBar(title = {
                Text(text = "Alarms List")
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
    }
}