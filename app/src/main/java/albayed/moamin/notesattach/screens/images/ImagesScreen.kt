package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.components.ImageElement
import albayed.moamin.notesattach.components.ImageViewer
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.navigation.Screens
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.*
import kotlin.collections.ArrayList


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(//right now using GlideImage with old GetContent() for getting images from gallery
    navController: NavController,
    noteId: String,
    viewModel: ImagesScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val imagesList = viewModel.images.collectAsState().value
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

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

    var uri: Uri? = null

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
        } else {
            Toast.makeText(context, " No Image Was Selected", Toast.LENGTH_LONG).show()
        }
    }

    val imagePicker = rememberLauncherForActivityResult(//older way of picking images
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
            viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            imageUri = uri
            if (imageUri != null) {
                viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))

            }
        }
    )

    val imageViewer =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }

    Scaffold(topBar = {
        TopBar(
            screen = Screens.ImagesScreen,
            navController = navController,
            firstAction = {
//                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                imagePicker.launch("image/*")
            },
            secondAction = {
                uri = ImagesFileProvider.getImageUri(context)
                cameraLauncher.launch(uri)
            },
            thirdAction = {
                if (!isDeleteMode.value) {
                    isDeleteMode.value = true
                } else if (isDeleteMode.value) {
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
            } else {
                //navController.navigate(Screens.MainScreen.name)
                navController.popBackStack()
            }
        } /*TODO check if this updates images counter in the note card on main screen*/
    }) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(imagesList) { image ->
                    ImageElement(
                        isViewImage = isViewImage,
                        viewImageUri = viewImageUri,
                        isDeleteMode = isDeleteMode,
                        isNewDeleteProcess = (imagesToDelete.isEmpty()),
                        image = image
                    ) { checkedDelete ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            imagesToDelete.remove(image)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            imagesToDelete.add(image)
                            Log.d("images", "ImagesScreen: ${imagesToDelete.size}")
                        }
                    }
                }
            }
            if (isViewImage.value) {
                ImageViewer(viewImageUri = viewImageUri)
            }
        }
    }
    if (isOpenDeleteDialog.value) {
        AlertDialog(onDismissRequest = { isOpenDeleteDialog.value = false },
            buttons = {
                TextButton(onClick = {
                    imagesToDelete.forEach { image -> viewModel.deleteImage(image) }
                    imagesToDelete.clear()
                    Log.d("size", "ImagesScreen: ${imagesToDelete.size}")
                    isOpenDeleteDialog.value = false
                    isDeleteMode.value = false
                }) {
                    Text(text = "Yes")
                }
                TextButton(onClick = { isOpenDeleteDialog.value = false }) {
                    Text(text = "No")
                }
            },
            title = {
                Text(text = "Are You Sure?")
            },
            text = {
                Text(text = "Are you sure you want to delete ${imagesToDelete.size} image(s)?")
            }
        )
    }
}

