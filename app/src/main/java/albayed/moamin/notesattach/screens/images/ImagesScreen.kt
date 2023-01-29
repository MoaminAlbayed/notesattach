package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(
    navController: NavController,
    noteId: String,
    viewModel: ImagesScreenViewModel = hiltViewModel()
) {
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


    val context = LocalContext.current
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
        }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            imageUri = uri
        }
    )
    //Log.d("imageUri", "ImagesScreen: $imageUri")
    Scaffold(topBar = {
        TopBar(
            screen = Screens.ImagesScreen,
            navController = navController,
            firstAction = { imagePicker.launch("image/*") },
            secondAction = {
                uri = ImagesFileProvider.getImageUri(context)
                cameraLauncher.launch(uri)
            }
        ) { navController.popBackStack() } /*TODO check if this updates images counter in the note card on main screen*/
    }) {

        Column() {
            if (hasImage && imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier.size(100.dp),
                    contentDescription = "Selected image",
                )
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

    }
}