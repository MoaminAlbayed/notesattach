package albayed.moamin.notesattach.screens.audioClips

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AudioClipCard
import albayed.moamin.notesattach.components.DeleteAlert
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
    val audioClips = viewModel.audioClips.collectAsState().value
    val audioClipsCount = viewModel.audioClipsCount.collectAsState().value

    val isDeleteMode = remember {
        mutableStateOf(false)
    }
    val audioClipsToDelete = remember {
        mutableStateListOf<AudioClip>()
    }
    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }

    val player by remember {
        mutableStateOf(MediaPlayer())
    }
    var isPlaying by rememberSaveable() {
        mutableStateOf(false)
    }
    var isPaused by rememberSaveable() {
        mutableStateOf(false)
    }

    var duration by remember {
        mutableStateOf(0f)
    }

    val currentPositionScope = rememberCoroutineScope()
    val permissionScope = rememberCoroutineScope()

    val currentPosition = remember {
        mutableStateOf<Float?>(null)
    }

    val audioClipCurrentlyPlaying = remember {
        mutableStateOf<File?>(null)
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("permission", "audioClipsScreen perm: permission denied")
            } else {
                Log.d("permission", "audioClipsScreen perm: permission granted")
                //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
            }
        }


    fun checkPermission(onGranted: () -> Unit = {}) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted.invoke()
            Log.d("permission", "audioClipsScreen: permission available")
            //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))

        } else {
            Log.d("permission", "audioClipsScreen: requesting")
//            SideEffect {
            permissionScope.launch {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    checkPermission()

    fun resumePlaying() {
        player.start()
        isPaused = false
    }

    fun pausePlaying() {
        player.pause()
        isPaused = true
    }

    fun onPause() {
        if (isPaused)
            resumePlaying()
        else
            pausePlaying()
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
                navController = navController,
                firstAction = {
                    if (audioClipsCount == 0) {
                        Toast.makeText(context, "No Audio Clips to Delete!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!isDeleteMode.value) {
                        isDeleteMode.value = true
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
                checkPermission() {//todo improve permission handling when rejecting
                    if (isDeleteMode.value) {
                        isDeleteMode.value = false
                    }
                    if (isPlaying)
                        stopPlaying()

                    navController.navigate(Screens.RecordAudioScreen.name + "/${noteId}")
                }
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
            items(audioClips.asReversed()) { audioClip ->
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
//                    if (isPlaying && file == audioClipCurrentlyPlaying.value) {
//                        stopPlaying()
//                        isPlaying = false
//                        audioClipCurrentlyPlaying.value = null
//                    } else {
//                        onPlay(file)
//                        isPlaying = true
//                        audioClipCurrentlyPlaying.value = audioClip.file
//                    }
                }
            }

        }
    }
    if (isOpenDeleteDialog.value) {
        DeleteAlert(
            isOpenDeleteDialog = isOpenDeleteDialog,
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
        } else
            if (isPlaying) {
                stopPlaying()
                player.release()
            }
        navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }

}


//    var player: MediaPlayer? = null


//    val currentPosition = rememberSaveable() {
//        mutableStateOf<Int?>(0)
//    }
//    val duration = rememberSaveable() {
//        mutableStateOf<Int?>(0)
//    }
//
//    val currentPositionState = produceState(initialValue = 0f) {
//        value = player.currentPosition.toFloat()
//    }.value
//
//
//
//    var currentPositionRemember = remember(player) {
//        mutableStateOf(player.currentPosition)
//    }


//    val sliderValue by remember {
//        mutableStateOf(0f)
//    }

//var currentPositionLaunched by remember {
//    mutableStateOf(0)
//}
//LaunchedEffect(key1 = Unit) {
//    while (true) {
//        currentPositionLaunched = player.currentPosition
//        Log.d("here", "AudioClipsScreen Launched: ${player.currentPosition}")
//        delay(1000)
//    }
//}


//newFile.value = NewFileProvider.getFileUri(context, FileTypes.AudioFile)


//        Button(onClick = {
//            onPlay()
//        }) {
//            Text(text = if (playing.value) "Stop Playing" else "Start Playing")
//        }
//
//
//
//        if (playing.value) {
//            Button(onClick = { onPause() }) {
//                Text(text = if (paused.value) "Resume" else "Pause")
//            }
//
//            Button(onClick = {
//                currentPosition.value = player?.currentPosition
//            }) {
//                Text(text = currentPosition.value.toString())
//            }
////            Text(text = currentPositionLaunched.toString())
////            Text(text = currentPositionState.toString())
//            Text(text = "remember: ${currentPositionRemember.value}")
//            Slider(
//                modifier = Modifier.fillMaxWidth(),
//                value = currentPositionLaunched.toFloat(),
//                onValueChange = {
//
//                },
//                valueRange = (0f..player.duration.toFloat())
//            )
//        }



