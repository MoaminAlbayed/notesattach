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
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TopBar(
    screen: Screens,
    firstAction: () -> Unit = {},
    isNewNote: Boolean = false,
    isMainScreenSearch: MutableState<Boolean> = mutableStateOf(false),
    //searchState: MutableState<String> = mutableStateOf(""),
    onSearchValueChanged: (String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    when (screen) {//todo refactor to make shorter
        Screens.MainScreen -> {
            TopAppBar(title = {
                if (isMainScreenSearch.value) {
                    val searchState = remember { mutableStateOf("") }
                    TextField(
                        value = searchState.value,
                        onValueChange = { value ->
                            searchState.value = value
                            onSearchValueChanged(value)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 2.dp, bottom = 2.dp),
                        textStyle = TextStyle(color = MaterialTheme.colors.primary),
                        placeholder = { Text(text = "Search...") },
                        trailingIcon = {
                            if (searchState.value != ("")) {
                                IconButton(
                                    onClick = {
                                        searchState.value = "" // Remove text from TextField when you press the 'X' icon
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(24.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(5.dp), // The TextFiled has rounded corners top left and right by default
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.primary,
                            cursorColor = MaterialTheme.colors.primary,
                            leadingIconColor = MaterialTheme.colors.primary,
                            trailingIconColor = MaterialTheme.colors.primary,
                            backgroundColor = MaterialTheme.colors.onPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                } else
                    Text(text = "My Notes")
            },
//                backgroundColor = Color.Black,
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    IconButton(onClick = { firstAction.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Button"
                        )
                    }
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More Options"
//                        )
//                    }
                }
            )
        }
        Screens.NoteEditor -> {
            if (!isNewNote) {
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
            TopAppBar(
                title = {
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
            TopAppBar(
                title = {
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