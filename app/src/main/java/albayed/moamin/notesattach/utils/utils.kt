package albayed.moamin.notesattach.utils



import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun dateFormatter(time: Long): String{
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(time))
}

fun fileDateFormatter(time: Long): String{
    val formatter = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
    return formatter.format(Date(time))
}

fun videoLength(context: Context, videoUri: Uri) :String{
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, videoUri)
    var videoTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
    return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(videoTime),
        TimeUnit.MILLISECONDS.toSeconds(videoTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(videoTime))
    )
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}