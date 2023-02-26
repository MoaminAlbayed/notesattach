package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.models.Image
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun ImageElement(
    modifier: Modifier = Modifier,
    isViewImage: MutableState<Boolean>,
    viewImageUri: MutableState<Uri?>,
    isDeleteMode: MutableState<Boolean>,
    image: Image,
    isNewDeleteProcess: Boolean,
    checkedDelete: (MutableState<Boolean>, Image) -> Unit
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
                            checkedDelete(isSelected, image)
                        } else {
                            isViewImage.value = true
                            viewImageUri.value = image.uri
                        }
                    },
                    onLongPress = {
                        isDeleteMode.value = true
                        checkedDelete(isSelected, image)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.uri)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "Attached Image"
        )
        if (isDeleteMode.value) {
            CircleCheckbox(selected = isSelected.value) {
                checkedDelete(isSelected, image)
            }
        }
    }
}



