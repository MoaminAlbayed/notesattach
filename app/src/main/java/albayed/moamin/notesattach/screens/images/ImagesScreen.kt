package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.navigation.Screens
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.util.*


@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(
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
    var uri: Uri? = null

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
            if (imageUri != null) {
                viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
            }
            navController.navigate(Screens.ImagesScreen.name + "/${noteId}")
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
            navController.navigate(Screens.ImagesScreen.name + "/${noteId}")
        }
    )

    Scaffold(topBar = {
        TopBar(
            screen = Screens.ImagesScreen,
            navController = navController,
            firstAction = {
                imagePicker.launch("image/*")
            },
            secondAction = {
                uri = ImagesFileProvider.getImageUri(context)
                cameraLauncher.launch(uri)
            }
        ) { navController.navigate(Screens.MainScreen.name) } /*TODO check if this updates images counter in the note card on main screen*/
    }) {

        Column() {
            //  if (hasImage && imageUri != null) {
            if (imagesList.isNotEmpty()) {
                imagesList.forEach {
                    Log.d("uri", "ImagesScreen: ${it.uri}")
                    GlideImage(
                        model = it.uri,
                        modifier = Modifier.size(100.dp),
                        contentDescription = null
                    )
//                    AsyncImage(//Coil is having trouble opening Uris of images from gallery
//                        //model = imageUri,
//                        model = it.uri,
//                        modifier = Modifier.size(100.dp),
//                        contentDescription = "Selected image",
//                    )
                }


            }
        }


    }
}


//        imagePicker.launch("image/*")


//
//        if (hasImage.value && imageUri.value != null) {
//            AsyncImage(
//                model = imageUri.value,
//                modifier = Modifier.fillMaxWidth(),
//                contentDescription = "Selected image",
//            )
//        }


//    val context = LocalContext.current
//
//        var hasImage by remember {
//            mutableStateOf(false)
//        }
//        var imageUri by remember {
//            mutableStateOf<Uri?>(null)
//        }
//    val uri = ImagesFileProvider.getImageUri(context)
//
//    Log.d("imageUri", "ImagesScreen: $imageUri")
//
//        val imagePicker = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.GetContent(),
//            onResult = { uri ->
//                hasImage = uri != null
//                imageUri = uri
//            }
//        )
//
//        val cameraLauncher = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.TakePicture(),
//            onResult = { success ->
//                hasImage = success
//                imageUri = uri
//            }
//        )
//
//
//        Box(
//
//        ) {
//            if (hasImage && imageUri != null) {
//                AsyncImage(
//                    model = imageUri,
//                    modifier = Modifier.fillMaxWidth(),
//                    contentDescription = "Selected image",
//                )
//            }
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 32.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Button(
//                    onClick = {
//                        imagePicker.launch("image/*")
//                    },
//                ) {
//                    Text(
//                        text = "Select Image"
//                    )
//                }
//                Button(
//                    modifier = Modifier.padding(top = 16.dp),
//                    onClick = {
//
//                        cameraLauncher.launch(uri)
//
//                    },
//                ) {
//                    Text(
//                        text = "Take photo"
//                    )
//                }
//            }
//        }
//    }
//