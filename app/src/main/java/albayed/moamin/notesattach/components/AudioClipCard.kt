package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.utils.dateFormatter
import albayed.moamin.notesattach.utils.formatTimer
import androidx.compose.animation.AnimatedVisibility
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


    Box(modifier = Modifier
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            elevation = 5.dp
        ) {
            Row(modifier = Modifier.fillMaxHeight()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        onClick(audioFile)
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
                    color = MaterialTheme.colors.onSurface
                )
                Column(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "Created: ${dateFormatter(audioClip.date.time)}",
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "Duration: " + formatTimer( if(audioClip.duration <1000L) 1000L else audioClip.duration),
                        fontSize = 14.sp
                    )
                    AnimatedVisibility(
                        visible = isPlaying.value,

                    ) {
                        Slider(
                            modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp),
                            value = currentPosition.value!!,
                            onValueChange = {
                                seekTo(it)
                            },
                            valueRange = (0f..duration!!),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.onSurface,
                                activeTrackColor = MaterialTheme.colors.onSurface
                            )
                        )
                    }

                }
            }
        }
        if (isDeleteMode.value) {
            CircleCheckbox(modifier = Modifier.align(Alignment.TopStart),
        selected = isSelected.value) {
                checkedDelete(isSelected)
            }
        }
    }

}