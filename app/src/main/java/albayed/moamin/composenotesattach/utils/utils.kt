package albayed.moamin.composenotesattach.utils


import albayed.moamin.composenotesattach.models.Alarm
import albayed.moamin.composenotesattach.models.Location
import albayed.moamin.composenotesattach.R
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun dateFormatter(time: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(time))
}

fun alarmDateFormatter(alarm: Alarm): String {
    return alarm.day.toString().padStart(2, '0') +
            "/${(alarm.month + 1).toString().padStart(2, '0')}" +
            "/${alarm.year}" +
            " ${alarm.hour.toString().padStart(2, '0')}" +
            ":${alarm.minute.toString().padStart(2, '0')}"
}

fun fileDateFormatter(time: Long): String {
    val formatter = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
    return formatter.format(Date(time))
}

fun videoLength(context: Context, videoUri: Uri): String {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, videoUri)
    val videoTime =
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
    retriever.close()
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(videoTime),
        TimeUnit.MILLISECONDS.toSeconds(videoTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(videoTime))
    )
}

fun formatTimer(timestamp: Long): String {
    val seconds = timestamp / 1000
    val secondsFormatted = (seconds % 60).pad(2)
    val minutes = seconds / 60
    val minutesFormatted = (minutes % 60).pad(2)
    val hours = minutes / 60
    return if (hours > 0) {
        val hoursFormatted = (minutes / 60).pad(2)
        "$hoursFormatted:$minutesFormatted"
    } else {
        "$minutesFormatted:$secondsFormatted"
    }
}

fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')

//BackPressHandler used to change behavior of system back button
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

@Composable
fun fetchThumbnailUri(location: Location): String {
    val baseUri = "https://maps.googleapis.com/maps/api/staticmap?"
    val zoom = 15
    val size = 200

    return "${baseUri}center=${location.latitude},${location.longitude}" +
            "&zoom=${zoom}&size=${size}x${size}&maptype=hybrid" +
            "&markers=color:red|${location.latitude},${location.longitude}" +
            "&key=${stringResource(id = R.string.maps_api_key_debug)}"
}


fun internetAvailable(context: Context): Boolean {
    val connectionManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)
    return if (networkCapabilities != null) {
        (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
                )
    } else
        false
}

@Composable
fun LockScreenOrientation( orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


fun checkPermissions (permissions: Array<String>, context: Context): Boolean{
    return permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

//GetContentActivityResult class used to request permission on button press
class GetContentActivityResult(
    private val launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    fun launch(permissions: Array<String>) {
        launcher.launch(permissions)
    }
}

@Composable
fun requestMyPermissions(route: String, context: Context, navController: NavController): GetContentActivityResult {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), onResult = { permissionsMap->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            navController.navigate(route = route)
        } else {
            Toast.makeText(context, "Permission Required", Toast.LENGTH_LONG).show()
        }
    })
    return remember(launcher) {
        GetContentActivityResult(launcher)
    }
}


