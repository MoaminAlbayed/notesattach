package albayed.moamin.notesattach.screens.images

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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


//    val requestPermissionLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (!isGranted) {
//                Log.d("permission", "ImageScreen perm: permission denied")
//            } else {
//                Log.d("permission", "ImageScreen perm: permission granted")
//                //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
//            }
//        }






    val imagesList = viewModel.images.collectAsState().value
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isViewImage by remember {
        mutableStateOf(false)
    }
    var viewImageUri by remember {
        mutableStateOf<Uri?>(null)
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
            }
        ) {
            if (isViewImage) {
                isViewImage = false
            } else {
                navController.navigate(Screens.MainScreen.name)
            }

        } /*TODO check if this updates images counter in the note card on main screen*/
    }) {

//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_MEDIA_IMAGES
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("permission", "ImageScreen: permission available")
//            //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
//
//        } else {
//            Log.d("permission", "ImageScreen: requesting")
//            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
//        }


        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(imagesList) { image ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(3.dp)
                            //.clickable { imageViewer.launch(Intent(Intent.ACTION_VIEW).setData(image.uri)) }
                            .clickable {
                                isViewImage = true
                                viewImageUri = image.uri
                            }
                    ) {
                        GlideImage(//using Glide because Coil has trouble reading images from picker uri
//                        AsyncImage(
                            model = image.uri,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Attached Image"
                        )
                    }

                }
            }
            if (isViewImage) {
                Surface(
                    modifier = Modifier.fillMaxSize(0.95f),
                    //modifier = Modifier.wrapContentSize(),
                    border = BorderStroke(width = 3.dp, color = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    GlideImage(model = viewImageUri, contentDescription = "Enlarged Image")
//                    AsyncImage(model = viewImageUri, contentDescription = "Enlarged Image")
                }
            }

        }

    }
}

