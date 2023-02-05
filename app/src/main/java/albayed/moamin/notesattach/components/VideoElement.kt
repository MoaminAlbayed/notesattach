package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.Video
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder

@Composable
fun VideoElement(
    modifier: Modifier = Modifier,
    isViewVideo: MutableState<Boolean>,
    viewVideoUri: MutableState<Uri?>,
    isDeleteMode: MutableState<Boolean>,
    video: Video,
    isNewDeleteProcess: Boolean,
    checkedDelete: (MutableState<Boolean>) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isSelected = remember {
        mutableStateOf(false)
    }
    if (isNewDeleteProcess) {
        isSelected.value = false
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .border(border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isDeleteMode.value) {
                            checkedDelete(isSelected)
                            Log.d("click", "ImageElement: ${isSelected.value}")
                        } else {
                            isViewVideo.value = true
                            viewVideoUri.value = video.uri
                        }
                    },
                    onLongPress = {
                        isDeleteMode.value = true
                        checkedDelete(isSelected)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }
    ) {

//        AsyncImage(
//            model = video.uri,
//            contentScale = ContentScale.Crop,
//            contentDescription = "Attached Image"
//        )
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components{
                add(VideoFrameDecoder.Factory())
            }.crossfade(true)
            .build()
        AsyncImage(model = video.uri, contentScale = ContentScale.Crop,contentDescription = "Attached Video", imageLoader = imageLoader)
        if (isDeleteMode.value) {
            CircleCheckbox(selected = isSelected.value) {
            }
        }
    }
}

