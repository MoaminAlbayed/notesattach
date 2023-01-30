package albayed.moamin.notesattach

import albayed.moamin.notesattach.navigation.Navigation
import albayed.moamin.notesattach.screens.MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import albayed.moamin.notesattach.ui.theme.NotesAttachTheme
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Log.d("permission", "onCreate: permission denied")
            } else {
                Log.d("permission", "onCreate: permission granted")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            NotesAttachTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //requestPermissions(LocalContext.current, this)
                    val context = LocalContext.current


                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d("permission", "onCreate: permission available")
                    } else {
                        Log.d("permission", "onCreate: requesting")
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    Navigation()
                }
            }
        }
    }

/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("result", "onRequestPermissionsResult: here")
        // this method is called after permissions has been granted.
        when (requestCode) {
            101 ->
                // in this case we are checking if the permissions are accepted or not.
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] === PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        // if the permissions are accepted we are displaying a toast message
                        // and calling a method to get image path.
                        Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                        //getImagePath(this)
                    } else {
                        // if permissions are denied we are closing the app and displaying the toast message.
                        Toast.makeText(
                            this,
                            "Permissions denied, Permissions are required to use the app..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

*/
}

/*

private fun checkPermission(ctx: Context): Boolean {
    // in this method we are checking if the permissions are granted or not and returning the result.
    val result = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
    Log.d("result", "checkPermission: $result")
    return result == PackageManager.PERMISSION_GRANTED
}

private fun requestPermissions(ctx: Context, activity: Activity) {
    if (checkPermission(ctx)) {
        // if the permissions are already granted we are calling
        // a method to get all images from our external storage.
        Toast.makeText(ctx, "Permissions granted..", Toast.LENGTH_SHORT).show()
        //getImagePath(ctx)
    } else {
        Log.d("result", "requestPermissions: here")
        // if the permissions are not granted we are
        // requesting permissions on below line
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
        )
    }
}

*/