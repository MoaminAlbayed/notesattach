package albayed.moamin.notesattach.screens.audioClips

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.ui.theme.NotesAttachTheme
import albayed.moamin.notesattach.utils.NewFileProvider
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
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
                    navController.popBackStack()
                }
            }
        },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.mic, contentDescription = "Use Mic Button") {
                if (isDeleteMode.value){
                    isDeleteMode.value = false
                }



            }
        }
    ) {


    }



//    var player: MediaPlayer? = null
    var player by remember {
        mutableStateOf(MediaPlayer())
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

    val currentPositionState = produceState(initialValue = 0f) {
        value = player.currentPosition.toFloat()
    }.value

    var currentPositionLaunched by remember {
        mutableStateOf(0)
    }

    var currentPositionRemember = remember(player) {
        mutableStateOf(player.currentPosition)
    }
    LaunchedEffect(key1 = Unit) {
        while (true) {
            currentPositionLaunched = player.currentPosition
            Log.d("here", "AudioClipsScreen Launched: ${player.currentPosition}")
            delay(1000)
        }
    }
    val sliderValue by remember {
        mutableStateOf(0f)
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


    //newFile.value = NewFileProvider.getFileUri(context, FileTypes.AudioFile)



    fun startPlaying() {
//
////        try {
////            player.setDataSource(newFile.value.file.toString())
////            player.setOnCompletionListener { playing.value = false }
////            player.prepare()
////            player.start()
////            playing.value = true
////        }catch (e: IOException){
////                Log.e("here", "startPlaying: ${e.localizedMessage}" )
////            }
//        player.apply {
//            try {
//                playing.value = true
//                setDataSource(newFile.value.file.toString())
//                Log.d("here", "startPlaying file: ${newFile.value.file}")
//                setOnCompletionListener {
//                    playing.value = false
//                    reset()
////                    release()
//
////                    player = null
//                }
//                prepare()
//                start()
//            } catch (e: IOException) {
//                Log.e("here", "startPlaying: ${e.localizedMessage}")
//            }
//        }
//        duration.value = player.duration
////        player?.prepare()
////        player?.start()
//
////        Log.d("here", "startPlaying: ${player?.duration}")
//
////        currentPosition = produceState(initialValue = 0){
////            value = player!!.currentPosition
////        }.value
//
//
//        //player.prepare()
////        player.start()

    }

    fun stopPlaying() {
        playing.value = false

//        player?.release()
        player?.reset()
//        player?.stop()
        //player = MediaPlayer()
    }


    fun onPlay() {
        if (playing.value)
            stopPlaying()
        else
            startPlaying()
    }

    fun resumePlaying() {
        player?.start()
        paused.value = false
    }

    fun pausePlaying() {
        player?.pause()
        paused.value = true
    }

    fun onPause() {
        if (paused.value)
            resumePlaying()
        else
            pausePlaying()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            onPlay()
        }) {
            Text(text = if (playing.value) "Stop Playing" else "Start Playing")
        }


        Log.d("here", "AudioClipsScreen positionState: $currentPositionState ")
        if (playing.value) {
            Button(onClick = { onPause() }) {
                Text(text = if (paused.value) "Resume" else "Pause")
            }

            Button(onClick = {
                currentPosition.value = player?.currentPosition
                Log.d("here", "AudioClipsScreen: button duration ${player?.duration}")
            }) {
                Text(text = currentPosition.value.toString())
            }
//            Text(text = currentPositionLaunched.toString())
//            Text(text = currentPositionState.toString())
            Text(text = "remember: ${currentPositionRemember.value}")
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = currentPositionLaunched.toFloat(),
                onValueChange = {

                },
                valueRange = (0f..player.duration.toFloat())
            )
        }


    }

}

@Preview
@Composable
fun AudioClipCard(onClick: () -> Unit = {}) {
    NotesAttachTheme() {//todo remove theme


        var playing by remember { mutableStateOf(false) }
        val icon = if (playing) R.drawable.stop_button else R.drawable.play_button
        val buttonContentDescription = if (playing) "Stop Playing Button" else "Play Button"
        Card(
            modifier = Modifier
                .padding(5.dp)
                .height(100.dp)
                .fillMaxWidth(),
            //.clickable { onClick.invoke() },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            elevation = 5.dp
        ) {
            Row {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { /*TODO onPlay*/
                        playing = !playing
                    }) {
                    Icon(
                        painter = painterResource(id = icon),
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
                        text = "Created: 01/02/2023 15:16",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Length: 4:02",
                        fontSize = 14.sp
                    )
                    AnimatedVisibility(//todo make text on top of slider move smoothly
                        visible = playing,
                        enter =
                        slideInVertically(initialOffsetY = { it }),
                        //expandVertically(expandFrom = Alignment.Bottom) +

                        exit =
                        //fadeOut() +
                        slideOutVertically(targetOffsetY = { it })
                    ) {
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = 0f,
                            onValueChange = {},
                            valueRange = (0f..100f),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.primary,
                                activeTrackColor = MaterialTheme.colors.primary
                            )
                        )
                    }

                }
            }
        }
    }
}