package albayed.moamin.composenotesattach.components

import albayed.moamin.composenotesattach.navigation.Screens
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import albayed.moamin.composenotesattach.R
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBar(
    screen: Screens,
    firstAction: () -> Unit = {},
    isNewNote: Boolean = false,
    isMainScreenSearch: MutableState<Boolean> = mutableStateOf(false),
    onSearchValueChanged: (String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val topBarColor = MaterialTheme.colors.primary
    val contentColor = MaterialTheme.colors.onPrimary

    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    val animationTime = 500

    when (screen) {
        Screens.MainScreen -> {
            AnimatedContent(
                targetState = isMainScreenSearch.value,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = animationTime),
                        initialOffsetX = {
                            if (isMainScreenSearch.value) it else -it
                        }
                    )  with
                            slideOutHorizontally(
                                animationSpec = tween(durationMillis = animationTime),
                                targetOffsetX = {
                                if (isMainScreenSearch.value) -it else it
                            }
                            )
                }
            ) {
                if (it){
                    TopAppBar(title = {
                        val searchState = remember { mutableStateOf("") }
                            TextField(
                                value = searchState.value,
                                onValueChange = { value ->
                                    searchState.value = value
                                    onSearchValueChanged(value)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 2.dp, bottom = 2.dp, end = 10.dp)
                                    .focusRequester(focusRequester),
                                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                                placeholder = {
                                    Text(
                                        text = "Search...",
                                        color = MaterialTheme.colors.primary
                                    )
                                },
                                trailingIcon = {
                                    if (searchState.value != ("")) {
                                        IconButton(
                                            onClick = {
                                                searchState.value = ""
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                tint = MaterialTheme.colors.primary,
                                                contentDescription = "Clear Search Button",
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(24.dp)
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(5.dp),
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
                    },
                        navigationIcon = {
                                IconButton(onClick = { firstAction.invoke() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back Button"
                                    )
                                }

                        },
                        backgroundColor = topBarColor,
                        contentColor = contentColor,
                    )
                } else {
                    TopAppBar(title = {
                            Text(text = "My Notes")
                    },
                        backgroundColor = topBarColor,
                        contentColor = contentColor,
                        actions = {
                            IconButton(onClick = {
                                firstAction.invoke()
                                if (isMainScreenSearch.value) {
                                    scope.launch {
                                        delay(100)
                                        focusRequester.requestFocus()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Button"
                                )
                            }
                        }
                    )
                }
            }
        }
        Screens.NoteEditor -> {
            if (!isNewNote) {
                TopAppBar(
                    title = {
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
                    },
                    backgroundColor = topBarColor,
                    contentColor = contentColor,
                )
            } else
                TopAppBar(
                    title = {
                        Text(text = "New Note")
                    },
                    navigationIcon = {
                        IconButton(onClick = { onClick.invoke() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                    },
                    backgroundColor = topBarColor,
                )
        }
        Screens.ImagesScreen -> {
            TopAppBar(
                title = {
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
                },
                backgroundColor = topBarColor,
                contentColor = contentColor,
            )
        }
        Screens.VideosScreen -> {
            TopAppBar(
                title = {
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
                },
                backgroundColor = topBarColor,
                contentColor = contentColor,
            )
        }
        Screens.AudioClipsScreen -> {
            TopAppBar(
                title = {
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
                },
                backgroundColor = topBarColor,
                contentColor = contentColor,
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
                backgroundColor = topBarColor,
                contentColor = contentColor,
            )
        }
        Screens.LocationsScreen -> {
            TopAppBar(
                title = {
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
                },
                backgroundColor = topBarColor,
                contentColor = contentColor,
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
                backgroundColor = topBarColor,
                contentColor = contentColor,
            )
        }
        Screens.AlarmsScreen -> {
            TopAppBar(
                title = {
                    Text(text = "Reminders List")
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
                },
                backgroundColor = topBarColor,
                contentColor = contentColor,
            )
        }
    }
}