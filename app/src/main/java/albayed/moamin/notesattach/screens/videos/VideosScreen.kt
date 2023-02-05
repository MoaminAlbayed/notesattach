package albayed.moamin.notesattach.screens.videos

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.ImageElement
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.components.VideoElement
import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import albayed.moamin.notesattach.models.Video
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import albayed.moamin.notesattach.utils.NewFileProvider
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.io.File
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VideosScreen(
    navController: NavController,
    noteId: String,
    viewModel: VideosScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val videosList = viewModel.videos.collectAsState().value
    val videosCount = viewModel.videosCount.collectAsState().value

    var hasVideo by remember {
        mutableStateOf(false)
    }
    val isViewVideo = remember {
        mutableStateOf(false)
    }
    val viewVideoUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val isDeleteMode = remember {
        mutableStateOf(false)
    }
    val videosToDelete = remember {
        mutableStateListOf<Video>()
    }
    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }
    val fileSaver = run {
        val fileKey = "file"
        val uriKey = "uri"
        mapSaver(
            save = { mapOf(fileKey to it.file!!.absolutePath, uriKey to it.uri.toString()) },
            restore = { FileInfo(File(it[fileKey].toString()), Uri.parse(it[uriKey].toString())) }
        )
    }

    val newFile = rememberSaveable(stateSaver = fileSaver) {
        mutableStateOf(FileInfo(File(""), Uri.EMPTY))
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { success ->
            hasVideo = success
            if (hasVideo) {
                viewModel.createVideo(
                    Video(
                        noteId = UUID.fromString(noteId),
                        uri = newFile.value.uri!!,
                        file = newFile.value.file!!
                    )
                )
                viewModel.updateVideosCount(videosCount = videosCount + 1, noteId = noteId)
            }
            if (!hasVideo) {
                if (newFile.value.file!!.exists()) {
                    newFile.value.file!!.delete()
                }
            }
        }
    )
    val videoViewer =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        }

    Scaffold(topBar = {
        TopBar(
            screen = Screens.VideosScreen,
            navController = navController,
            firstAction = {
                if (videosCount == 0) {
                    Toast.makeText(context, "No Images to Delete!", Toast.LENGTH_SHORT).show()
                } else if (!isDeleteMode.value) {
                    isDeleteMode.value = true
                } else {
                    if (videosToDelete.isNotEmpty()) {
                        isOpenDeleteDialog.value = true
                    } else {
                        isDeleteMode.value = false
                    }
                }
            }
        ) {
            if (isViewVideo.value) {
                isViewVideo.value = false
            } else if (isDeleteMode.value) {
                isDeleteMode.value = false
                videosToDelete.clear()
            } else {
                navController.popBackStack()
            }
        }
    },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.video, contentDescription = "Use Camera Button") {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                }
//                newFile.value = ImagesFileProvider.getImageUri(context, fileDateFormatter(Date.from(
//                    Instant.now()).time))
//                newFile.value = ImagesFileProvider.getImageUri(context)
                newFile.value = NewFileProvider.getFileUri(context, FileTypes.VideoFile)
                cameraLauncher.launch(newFile.value!!.uri)
            }
        }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 6.dp, end = 6.dp),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(videosList) { video ->
                    VideoElement(
                        isViewVideo = isViewVideo,
                        viewVideoUri = viewVideoUri,
                        isDeleteMode = isDeleteMode,
                        isNewDeleteProcess = (videosToDelete.isEmpty()),
                        video = video
                    ) { checkedDelete ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            videosToDelete.remove(video)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            videosToDelete.add(video)
                        }
                    }
                }
            }
            if (isViewVideo.value) {
                videoViewer.launch(
                    Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .setData(viewVideoUri.value)
                )
                isViewVideo.value = false

            }
        }
    }
    if (isOpenDeleteDialog.value) {
        AlertDialog(onDismissRequest = { isOpenDeleteDialog.value = false },
            buttons = {
                TextButton(onClick = {
                    videosToDelete.forEach { video ->
                        viewModel.deleteVideo(video)
                    }
                    viewModel.updateVideosCount(
                        videosCount = videosCount - videosToDelete.size,
                        noteId = noteId
                    )
                    videosToDelete.clear()
                    isOpenDeleteDialog.value = false
                    isDeleteMode.value = false
                }) {
                    Text(text = "Yes", style = MaterialTheme.typography.button)
                }
                TextButton(onClick = {
                    isOpenDeleteDialog.value = false
                    videosToDelete.clear()
                    isDeleteMode.value = false
                }) {
                    Text(text = "No", style = MaterialTheme.typography.button)
                }
            },
            title = {
                Text(text = "Deleting Images", style = MaterialTheme.typography.h6)
            },
            text = {
                Text(
                    text = "Are you sure you want to delete ${videosToDelete.size} video(s)?",
                    style = MaterialTheme.typography.body1
                )
            }
        )
    }
    fun backToMainScreen() {
        if (isDeleteMode.value) {
            isDeleteMode.value = false
            videosToDelete.clear()
        } else
            navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }
}