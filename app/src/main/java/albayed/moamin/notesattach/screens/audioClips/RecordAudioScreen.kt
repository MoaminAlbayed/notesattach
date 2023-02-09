package albayed.moamin.notesattach.screens.audioClips

import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.NewFileProvider
import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.io.File
import java.io.IOException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecordAudioScreen(
    navController: NavController,
    noteId: String,
    viewModel: AudioClipsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val audioClipsCount = viewModel.audioClipsCount.collectAsState().value
    val fileSaver = run {
        val fileKey = "file"
        val uriKey = "uri"
        mapSaver(
            save = { mapOf(fileKey to it.file!!.absolutePath, uriKey to it.uri.toString()) },
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

    fun startRecording() {
        isRecording.value = true
        recorded = true
        recorder = MediaRecorder(context).apply {
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
        isRecording.value = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        viewModel.createAudioClip(
            AudioClip(
                noteId = noteId,
                duration = 0,
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
        TopBar(screen = Screens.RecordAudioScreen, navController = navController) {
            if (isRecording.value) {
                stopRecording()
            } else if (!recorded) {
                newFile.value.file.delete()
                navController.popBackStack()
            }


        }
    }) {
        Button(onClick = {
            onRecord()
        }) {
            Text(text = if (isRecording.value) "Stop" else "Start")
        }
    }




}