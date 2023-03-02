package albayed.moamin.composenotesattach.screens.audioClips

import albayed.moamin.composenotesattach.R
import albayed.moamin.composenotesattach.components.AudioClipCard
import albayed.moamin.composenotesattach.components.ConfirmMessage
import albayed.moamin.composenotesattach.components.FloatingButton
import albayed.moamin.composenotesattach.components.TopBar
import albayed.moamin.composenotesattach.models.AudioClip
import albayed.moamin.composenotesattach.navigation.Screens
import albayed.moamin.composenotesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AudioClipsScreen(
    navController: NavController,
    noteId: String,
    viewModel: AudioClipsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val audioClips = viewModel.audioClips.collectAsState().value
    val audioClipsCount = viewModel.audioClipsCount.collectAsState().value

    val isDeleteMode = rememberSaveable {
        mutableStateOf(false)
    }
    val audioClipsToDelete = remember {
        mutableStateListOf<AudioClip>()
    }
    val isOpenDeleteDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val player by remember() {
        mutableStateOf(MediaPlayer())
    }
    var isPlaying by rememberSaveable() {
        mutableStateOf(false)
    }
    var isPaused by rememberSaveable() {
        mutableStateOf(false)
    }

    var duration by rememberSaveable {
        mutableStateOf(0f)
    }

    val currentPositionScope = rememberCoroutineScope()


    val currentPosition = rememberSaveable {
        mutableStateOf<Float?>(null)
    }

    val audioClipCurrentlyPlaying = rememberSaveable {
        mutableStateOf<File?>(null)
    }


    fun resumePlaying() {
        player.start()
        isPaused = false
    }

    fun pausePlaying() {
        player.pause()
        isPaused = true
    }


    fun startPlaying(file: File) {

        player.apply {
            try {
                isPlaying = true
                setDataSource(file.toString())
                setOnCompletionListener {
                    reset()
                    isPlaying = false
                    audioClipCurrentlyPlaying.value = null
                    duration = 0f
                    currentPositionScope.coroutineContext.cancelChildren()
                }
                prepare()
                start()
                duration = player.duration.toFloat()
                currentPositionScope.launch {
                    //periodically read current position from the player and stored in current position var that is used by the slider
                    while (isActive) {
                        currentPosition.value = player.currentPosition.toFloat()
                        delay(15)
                    }
                }
            } catch (e: IOException) {
                Log.e("here", "startPlaying: ${e.localizedMessage}")
            }
        }
    }

    fun stopPlaying() {
        player.reset()
        currentPositionScope.coroutineContext.cancelChildren()
        isPaused = false
        isPlaying = false
        audioClipCurrentlyPlaying.value = null
    }


    fun onPlay(file: File) {
        if (isPlaying) {
            stopPlaying()
            startPlaying(file)
        } else
            startPlaying(file)
    }

    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.AudioClipsScreen,
                firstAction = {
                    if (audioClipsCount == 0) {
                        Toast.makeText(context, "No Audio Clips to Delete!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!isDeleteMode.value) {
                        isDeleteMode.value = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                    } else {
                        if (audioClipsToDelete.isNotEmpty()) {
                            if (isPlaying)
                                stopPlaying()
                            isOpenDeleteDialog.value = true
                        } else {
                            isDeleteMode.value = false
                        }
                    }
                }
            ) {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                    audioClipsToDelete.clear()
                } else {
                    if (isPlaying) {
                        stopPlaying()
                        player.release()
                    }
                    navController.popBackStack()
                }
            }
        },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.mic, contentDescription = "Use Mic Button") {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                }
                if (isPlaying)
                    stopPlaying()
                navController.navigate(Screens.RecordAudioScreen.name + "/${noteId}")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(audioClips) { audioClip ->
                AudioClipCard(
                    audioClip = audioClip,
                    duration = duration,
                    isDeleteMode = isDeleteMode,
                    isNewDeleteProcess = audioClipsToDelete.isEmpty(),
                    audioClipCurrentlyPlaying = audioClipCurrentlyPlaying,
                    currentPosition = currentPosition,
                    seekTo = {
                        player.seekTo(it.toInt())
                    },
                    stopPlaying = {
                        stopPlaying()
                        isPlaying = false
                        audioClipCurrentlyPlaying.value = null
                    },
                    isPaused = isPaused,
                    checkedDelete = { checkedDelete ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            audioClipsToDelete.remove(audioClip)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            audioClipsToDelete.add(audioClip)
                        }
                    }
                ) { file ->
                    if (isPlaying && file == audioClipCurrentlyPlaying.value) {
                        if (isPaused)
                            resumePlaying()
                        else
                            pausePlaying()
                    } else {
                        onPlay(file)
                        isPlaying = true
                        audioClipCurrentlyPlaying.value = audioClip.file
                    }
                }
            }

        }
    }
    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                audioClipsToDelete.forEach { audioClip ->
                    viewModel.deleteAudioClip(audioClip)
                }
                viewModel.updateAudioClipsCount(
                    audioClipsCount = audioClipsCount - audioClipsToDelete.size,
                    noteId = noteId
                )
                audioClipsToDelete.clear()
                isOpenDeleteDialog.value = false
                isDeleteMode.value = false
            },
            onClickNo = {
                isOpenDeleteDialog.value = false
                audioClipsToDelete.clear()
                isDeleteMode.value = false
            },
            title = "Deleting Images",
            text = "Are you sure you want to delete ${audioClipsToDelete.size} audio clip(s)?"
        )
    }
    fun backToMainScreen() {
        if (isDeleteMode.value) {
            isDeleteMode.value = false
            audioClipsToDelete.clear()
        } else {
            if (isPlaying) {
                stopPlaying()
                player.release()
            }
            navController.popBackStack()
        }
    }
    BackPressHandler() {
        backToMainScreen()
    }

}

