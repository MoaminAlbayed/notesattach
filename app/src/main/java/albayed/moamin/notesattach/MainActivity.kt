package albayed.moamin.notesattach

import albayed.moamin.notesattach.navigation.Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import albayed.moamin.notesattach.ui.theme.NotesAttachTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        var permissionsLauncher: ActivityResultLauncher<Array<String>>
//        var isFirstPermissionGranted = false
//        var isSecondPermissionGranted = false
//
//        permissionsLauncher = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
//            isFirstPermissionGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES]?:isFirstPermissionGranted
//            isSecondPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]?:isSecondPermissionGranted
//        }
//
//        isFirstPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
//        isSecondPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//
//        val permissionRequest: MutableList<String> = ArrayList()
//        if (!isFirstPermissionGranted){
//            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
//        }
//        if (!isSecondPermissionGranted){
//            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//        if (permissionRequest.isNotEmpty()){
//                Log.d("permissions", "MainActivity: $permissionRequest")
//                permissionsLauncher.launch(permissionRequest.toTypedArray())
//
//        }


        setContent {
            NotesAttachTheme {
                // A surface container using the 'background' color from the theme
                Surface(
//                     modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
//                         colors = listOf(
//                             MaterialTheme.colors.primary,
//                             MaterialTheme.colors.onPrimary
//                         )
//                     )),
                    color = MaterialTheme.colors.background
//                color = Brush.verticalGradient(
//                    colors = listOf(
//                        MaterialTheme.colors.primary,
//                        MaterialTheme.colors.onPrimary
//                    )
//                )
                ) {
                    Navigation()
                }
            }
        }
    }
}

