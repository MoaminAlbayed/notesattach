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
import android.widget.SeekBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
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
    val currentPosition = rememberSaveable() {
        mutableStateOf<Int?>(0)
    }
    val duration = rememberSaveable() {
        mutableStateOf<Int?>(0)
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

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        == PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("permission", "audioClipsScreen: permission available")
        //viewModel.createImage(Image(noteId = UUID.fromString(noteId), uri = imageUri!!))

    } else {
        Log.d("permission", "audioClipsScreen: requesting")
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

//        try {
//            player.setDataSource(newFile.value.file.toString())
//            player.setOnCompletionListener { playing.value = false }
//            player.prepare()
//            player.start()
//            playing.value = true
//        }catch (e: IOException){
//                Log.e("here", "startPlaying: ${e.localizedMessage}" )
//            }
        player = MediaPlayer().apply {
            try{
                playing.value = true
                setDataSource(newFile.value.file.toString())
                setOnCompletionListener {
                    playing.value = false
//                    release()
//                    player = null
                }
                prepare()
                start()
            }catch (e: IOException){
                Log.e("here", "startPlaying: ${e.localizedMessage}" )
            }
        }

        Log.d("here", "startPlaying: ${player?.duration}")
        duration.value = player?.duration
        playing.value = true
        //player.prepare()
//        player.start()
        
    }
    fun stopPlaying(){
        playing.value = false
        player?.release()
//        player?.stop()
        //player = MediaPlayer()
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

        
        
        if(playing.value) {
            Button(onClick = { onPause() }) {
                Text(text = if (paused.value) "Resume" else "Pause")
            }
            Button(onClick = {
                currentPosition.value = player?.duration
                Log.d("here", "AudioClipsScreen: button ${player?.duration}")
            }) {
                Text(text = currentPosition.value.toString())
            }
            Text(text = duration.value.toString())
            Slider(modifier = Modifier.fillMaxWidth(), value = 0f, onValueChange = {})
//            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = player?.currentPosition!!.toFloat())
        }
            

    }

}