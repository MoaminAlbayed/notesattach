package albayed.moamin.notesattach.screens.audioClips

import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import albayed.moamin.notesattach.utils.NewFileProvider
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.io.File
import java.io.IOException

@Composable
fun AudioClipsScreen(
    navController: NavController,
    noteId: String,
    viewModel: AudioClipsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val audioClips = viewModel.audioClips.collectAsState().value
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
        mutableStateOf(FileInfo(File(""), Uri.EMPTY))
    }

    var recorder: MediaRecorder? = null
    var player: MediaPlayer? = null
    val recording = rememberSaveable() {
        mutableStateOf(false)
    }
    val playing = rememberSaveable() {
        mutableStateOf(false)
    }
    val paused = rememberSaveable() {
        mutableStateOf(false)
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("permission", "ImageScreen perm: permission denied")
            } else {
                Log.d("permission", "ImageScreen perm: permission granted")
                //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))
            }
        }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        == PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("permission", "ImageScreen: permission available")
        //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))

    } else {
        Log.d("permission", "ImageScreen: requesting")
        SideEffect {

            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }



    newFile.value = NewFileProvider.getFileUri(context, FileTypes.AudioFile)

    fun startRecording() {
        recording.value = true
        recorder = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFile(newFile.value.file)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            try {
                prepare()
            } catch (e: IOException){
                Log.e("here", "starRecording: ${e.localizedMessage}" )
            }
            start()
        }
    }



    fun stopRecording() {
        recording.value = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun onRecord (){
        if (recording.value)
            stopRecording()
        else
            startRecording()
    }
    fun startPlaying(){
        playing.value = true
        player = MediaPlayer().apply {
            try{
                setDataSource(newFile.value.file.toString())
                setOnCompletionListener {
                    playing.value = false
                }
                prepare()
                start()
            }catch (e: IOException){
                Log.e("here", "startPlaying: ${e.localizedMessage}" )
            }
        }
    }
    fun stopPlaying(){
        playing.value = false
        player?.release()
        player = null
    }

    fun onPlay(){
        if (playing.value)
            stopPlaying()
        else
            startPlaying()
    }

    fun resumePlaying(){
        player?.start()
        paused.value = false
    }
    fun pausePlaying(){
        player?.pause()
        paused.value = true
    }

    fun onPause (){
        if(paused.value)
            resumePlaying()
        else
            pausePlaying()
    }




    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onRecord() }) {
            Text(text = if (recording.value) "Stop Recording" else "Start Recording")
        }
        Button(onClick = { onPlay() }) {
            Text(text = if (playing.value) "Stop Playing" else "Start Playing")
        }
        Button(onClick = { onPause() }) {
            Text(text = if (paused.value) "Resume" else "Pause")
        }

    }

}