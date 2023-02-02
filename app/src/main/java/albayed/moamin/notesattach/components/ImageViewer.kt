package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.utils.ZoomableImage
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImageViewer (viewImageUri: MutableState<Uri?>){
    Surface(
        modifier = Modifier.fillMaxSize(0.95f),
        //modifier = Modifier.wrapContentSize(),
        border = BorderStroke(width = 3.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(20.dp)
    ) {
//        AsyncImage(model = viewImageUri.value, contentDescription = "Enlarged Image")
        ZoomableImage(painter = rememberAsyncImagePainter(model = viewImageUri.value), isRotation = false)
    }
}