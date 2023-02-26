package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.ImageElement
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import albayed.moamin.notesattach.utils.NewFileProvider
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.io.File
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(
    navController: NavController,
    noteId: String,
    viewModel: ImagesScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val imagesList = viewModel.images.collectAsState().value
    val imagesCount = viewModel.imagesCount.collectAsState().value

    val isViewImage = remember {
        mutableStateOf(false)
    }
    val viewImageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val isDeleteMode = remember {
        mutableStateOf(false)
    }

    val imagesToDelete = remember {
        mutableStateListOf<Image>()
    }

    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }

    val fileSaver = run {
        val fileKey = "file"
        val uriKey = "uri"
        mapSaver(
            save ={ mapOf(fileKey to it.file.absolutePath, uriKey to it.uri.toString()) },
            restore = {FileInfo(File(it[fileKey].toString()), Uri.parse(it[uriKey].toString()))}
        )
    }

    val newFile = rememberSaveable(stateSaver = fileSaver){
        mutableStateOf(FileInfo(File(""), Uri.EMPTY))
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                viewModel.createImage(
                    Image(
                        noteId = UUID.fromString(noteId),
                        uri = newFile.value.uri,
                        file = newFile.value.file
                    )
                )
                viewModel.updateImagesCount(imagesCount = imagesCount + 1, noteId = noteId)
            }
            if (!success) {
                if (newFile.value.file.exists()) {
                    newFile.value.file.delete()
                }
            }
        }
    )

    val imageViewer =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        }

    Scaffold(topBar = {
        TopBar(
            screen = Screens.ImagesScreen,
            firstAction = {
                if (imagesCount == 0) {
                    Toast.makeText(context, "No Images to Delete!", Toast.LENGTH_SHORT).show()
                } else if (!isDeleteMode.value) {
                    isDeleteMode.value = true
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                } else {
                    if (imagesToDelete.isNotEmpty()) {
                        isOpenDeleteDialog.value = true
                    } else {
                        isDeleteMode.value = false
                    }
                }
            }
        ) {
            if (isViewImage.value) {
                isViewImage.value = false
            } else if (isDeleteMode.value) {
                isDeleteMode.value = false
                imagesToDelete.clear()
            } else {
                navController.popBackStack()
            }
        }
    },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.camera, contentDescription = "Use Camera Button") {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                    imagesToDelete.clear()
                }
                newFile.value = NewFileProvider.getFileUri(context, FileTypes.ImageFile)
                cameraLauncher.launch(newFile.value.uri)
            }
        }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 6.dp, end = 6.dp),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(imagesList) { image ->
                    ImageElement(
                        isViewImage = isViewImage,
                        viewImageUri = viewImageUri,
                        isDeleteMode = isDeleteMode,
                        isNewDeleteProcess = (imagesToDelete.isEmpty()),
                        image = image
                    ) { checkedDelete, selectedImage ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            imagesToDelete.remove(selectedImage)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            imagesToDelete.add(selectedImage)
                        }
                    }
                }
            }
            if (isViewImage.value) {
                imageViewer.launch(Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).setData(viewImageUri.value))
                isViewImage.value = false

            }
        }
    }
    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                imagesToDelete.forEach { image ->
                    viewModel.deleteImage(image)
                }
                viewModel.updateImagesCount(
                    imagesCount = imagesCount - imagesToDelete.size,
                    noteId = noteId
                )
                imagesToDelete.clear()
                isOpenDeleteDialog.value = false
                isDeleteMode.value = false
            },
            onClickNo = {
                isOpenDeleteDialog.value = false
                imagesToDelete.clear()
                isDeleteMode.value = false
            },
            title = "Deleting Images",
            text = "Are you sure you want to delete ${imagesToDelete.size} image(s)?"
        )
    }
    fun backToMainScreen() {
        if (isDeleteMode.value) {
            isDeleteMode.value = false
            imagesToDelete.clear()
        } else
            navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }
}



