package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.models.Video
import albayed.moamin.notesattach.utils.videoLength
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.ImageLoader
import coil.compose.AsyncImage
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


    ConstraintLayout(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .border(border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isDeleteMode.value) {
                            checkedDelete(isSelected)
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

        val (checkMarkRef, timerRef) = createRefs()
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }.crossfade(true)
            .build()
        AsyncImage(
            model = video.uri,
            contentScale = ContentScale.Crop,
            contentDescription = "Attached Video",
            imageLoader = imageLoader
        )
        Surface(modifier = Modifier
            .constrainAs(timerRef) {
            start.linkTo(parent.start, margin = 5.dp)
            bottom.linkTo(parent.bottom, margin = 5.dp)
        },
            shape = RoundedCornerShape(size = 5.dp),
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(modifier = Modifier.size(20.dp), imageVector = Icons.Default.PlayArrow, tint = Color.White, contentDescription = "")
                Text(modifier = Modifier.padding(end = 3.dp), text = videoLength(context, video.uri), style = MaterialTheme.typography.caption, color = Color.White)
            }
        }
        if (isDeleteMode.value) {
            CircleCheckbox(modifier = Modifier.constrainAs(checkMarkRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }, selected = isSelected.value) {
                checkedDelete(isSelected)
            }
        }
    }
}

