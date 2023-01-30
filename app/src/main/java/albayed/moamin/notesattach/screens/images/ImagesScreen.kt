package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.navigation.Screens
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import java.io.ByteArrayOutputStream
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

//    val imagePicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri ->
//            hasImage = uri != null
//            imageUri = uri
//            if (imageUri != null) {
//                viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
//            }
//            navController.navigate(Screens.ImagesScreen.name + "/${noteId}")
//        }
//    )
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            val thumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(imageUri.toString()),
                128,
                128
            )
            val thumbnailByteArray = BitmapToByteArray(thumbImage)
            viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!, thumbnail = thumbnailByteArray))
        } else {
            Toast.makeText(context, " No Image Was Selected", Toast.LENGTH_LONG).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            imageUri = uri
            if (imageUri != null) {
                val thumbImage = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(imageUri!!.path),
                    128,
                    128
                )
                val thumbnailByteArray = BitmapToByteArray(thumbImage)
                viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!, thumbnail = thumbnailByteArray))

            }
            navController.navigate(Screens.ImagesScreen.name + "/${noteId}")
        }
    )

    Scaffold(topBar = {
        TopBar(
            screen = Screens.ImagesScreen,
            navController = navController,
            firstAction = {
                pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            secondAction = {
                uri = ImagesFileProvider.getImageUri(context)
                cameraLauncher.launch(uri)
            }
        ) { navController.navigate(Screens.MainScreen.name) } /*TODO check if this updates images counter in the note card on main screen*/
    }) {


        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
            items(imagesList) { image ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(3.dp)
                ) {
                    //GlideImage(
                    AsyncImage(
                        model = ByteArrayToBitmap(image.thumbnail),
                        //model = image.uri,
                        //modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }

            }
        }

    }
}

fun BitmapToByteArray( image: Bitmap): ByteArray{

    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

fun ByteArrayToBitmap(data: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(data, 0, data.size)
}


