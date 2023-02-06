package albayed.moamin.notesattach.utils



import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
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

fun videoLengthFormatter(milliseconds: Long) :String{
//    // long minutes = (milliseconds / 1000) / 60;
//    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
//
//    // long seconds = (milliseconds / 1000);
//    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

//    return "$minutes:$seconds"
    return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
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