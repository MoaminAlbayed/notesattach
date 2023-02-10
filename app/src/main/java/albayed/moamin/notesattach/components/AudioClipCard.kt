package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.utils.dateFormatter
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import java.io.File

@Composable
fun AudioClipCard(
    audioClip: AudioClip,
    duration: Float?,
    isDeleteMode: MutableState<Boolean>,
    isNewDeleteProcess: Boolean,
    checkedDelete: (MutableState<Boolean>) -> Unit,
    audioClipCurrentlyPlaying: MutableState<File?>,
    isPaused: Boolean,
    currentPosition: MutableState<Float?>,
    seekTo: (Float) -> Unit,
    stopPlaying: () -> Unit,
    onClick: (audioFile: File) -> Unit,
) {
    val audioFile = audioClip.file
    val isPlaying = remember { mutableStateOf(false) }
    isPlaying.value = audioFile == audioClipCurrentlyPlaying.value
    val firstButton = if (isPlaying.value && !isPaused) R.drawable.pause_button else R.drawable.play_button
    val secondButton = R.drawable.stop_button
    val buttonContentDescription = if (isPlaying.value) "Stop Playing Button" else "Play Button"

    val haptic = LocalHapticFeedback.current
    val isSelected = remember {
        mutableStateOf(false)
    }
    if (isNewDeleteProcess) {
        isSelected.value = false
    }

    ConstraintLayout(modifier = Modifier
        .padding(5.dp)
        .height(100.dp)
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    if (isDeleteMode.value) {
                        checkedDelete(isSelected)
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
        val checkRef = createRef()
        Card(
            modifier = Modifier.fillMaxWidth(),
            //.clickable { onClick.invoke() },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            elevation = 5.dp
        ) {
            Row() {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        onClick(audioFile)
                        Log.d("here", "isPlaying: ${isPlaying.value} isPaused: $isPaused")
                    }) {
                    Icon(
                        painter = painterResource(id = firstButton),
                        contentDescription = buttonContentDescription
                    )
                }
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        stopPlaying()
                        Log.d("here", "isPlaying: ${isPlaying.value} isPaused: $isPaused")
                    },
                    enabled = isPlaying.value
                ) {
                    Icon(
                        painter = painterResource(id = secondButton),
                        contentDescription = buttonContentDescription
                    )
                }
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight(0.9f)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.primary
                )
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Created: ${dateFormatter(audioClip.date.time)}",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Length: 4:02",
                        fontSize = 14.sp
                    )
                    AnimatedVisibility(//todo make text on top of slider move smoothly
                        visible = isPlaying.value,
                        enter =
                        slideInVertically(initialOffsetY = { it }),
                        //expandVertically(expandFrom = Alignment.Bottom) +

                        exit =
                        //fadeOut() +
                        slideOutVertically(targetOffsetY = { it })
                    ) {
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
//                            value = 1f,
                            value = currentPosition.value!!,
                            onValueChange = {
                                seekTo(it)
                            },
                            valueRange = (0f..duration!!),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.primary,
                                activeTrackColor = MaterialTheme.colors.primary
                            )
                        )
                    }

                }
            }
        }
        if (isDeleteMode.value) {
            CircleCheckbox(modifier = Modifier.constrainAs(checkRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }, selected = isSelected.value) {
                checkedDelete(isSelected)
            }
        }
    }

}