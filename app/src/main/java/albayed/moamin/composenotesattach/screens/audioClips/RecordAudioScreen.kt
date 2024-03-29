package albayed.moamin.composenotesattach.screens.audioClips

import albayed.moamin.composenotesattach.R
import albayed.moamin.composenotesattach.components.TopBar
import albayed.moamin.composenotesattach.models.AudioClip
import albayed.moamin.composenotesattach.models.FileInfo
import albayed.moamin.composenotesattach.models.FileTypes
import albayed.moamin.composenotesattach.navigation.Screens
import albayed.moamin.composenotesattach.utils.BackPressHandler
import albayed.moamin.composenotesattach.utils.LockScreenOrientation
import albayed.moamin.composenotesattach.utils.NewFileProvider
import albayed.moamin.composenotesattach.utils.formatTimer
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecordAudioScreen(
    navController: NavController,
    noteId: String,
    viewModel: AudioClipsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val audioClipsCount = viewModel.audioClipsCount.collectAsStateWithLifecycle().value
    //file saver used for saving file info when the screen rotation is changed,
    // currently not used because screen orientation is locked to portrait in record screen
    val fileSaver = run {
        val fileKey = "file"
        val uriKey = "uri"
        mapSaver(
            save = { mapOf(fileKey to it.file.absolutePath, uriKey to it.uri.toString()) },
            restore = { FileInfo(File(it[fileKey].toString()), Uri.parse(it[uriKey].toString())) }
        )
    }
    val newFile = rememberSaveable(stateSaver = fileSaver) {
        mutableStateOf(NewFileProvider.getFileUri(context, FileTypes.AudioFile))
    }
    var recorded by remember { mutableStateOf(false) }

    val isRecording = rememberSaveable() {
        mutableStateOf(false)
    }
    var recorder: MediaRecorder? = null

    var startingTime: Long = 0
    var duration: Long

    val timerScope = rememberCoroutineScope()

    var timer: Long = 0
    var timerString by remember {
        mutableStateOf("00:00")
    }

    fun startRecording() {
        startingTime = System.currentTimeMillis()
        timerScope.launch {
            while (isActive) {
                //periodically updates the timer
                timerString = formatTimer(timer)
                timer += 1000
                delay(1000)
            }
        }
        isRecording.value = true
        recorded = true
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFile(newFile.value.file)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("here", "starRecording: ${e.localizedMessage}")
            }
            start()
        }
    }


    fun stopRecording() {
        duration = System.currentTimeMillis() - startingTime
        timerScope.coroutineContext.cancelChildren()
        isRecording.value = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        viewModel.createAudioClip(
            AudioClip(
                noteId = UUID.fromString(noteId),
                duration = duration,
                uri = newFile.value.uri,
                file = newFile.value.file
            )
        )
        viewModel.updateAudioClipsCount(audioClipsCount = audioClipsCount + 1, noteId = noteId)
        navController.popBackStack()
    }

    fun onRecord() {
        if (isRecording.value)
            stopRecording()
        else
            startRecording()
    }

    Scaffold(topBar = {
        TopBar(screen = Screens.RecordAudioScreen) {
            if (isRecording.value) {
                stopRecording()
            } else if (!recorded) {
                newFile.value.file.delete()
                navController.popBackStack()
            }
        }
    }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = timerString, fontSize = 35.sp, color = Color.White)
            Spacer(modifier = Modifier.size(50.dp))
            IconButton(onClick = { onRecord() }) {
                if (isRecording.value) {
                    Icon(
                        modifier = Modifier.scale(2f),
                        painter = painterResource(id = R.drawable.stop_button),
                        tint = Color.White,
                        contentDescription = "Stop Recording Button"
                    )
                } else {
                    Icon(
                        modifier = Modifier.scale(2f),
                        painter = painterResource(id = R.drawable.record_button),
                        tint = Color.Red,
                        contentDescription = "Record Button"
                    )
                }
            }
        }
    }
    fun backToMainScreen() {
        if (isRecording.value) {
            stopRecording()
        } else if (!recorded) {
            newFile.value.file.delete()
            navController.popBackStack()
        }
    }
    BackPressHandler() {
        backToMainScreen()
    }
}