package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.ImageFile
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

class ImagesFileProvider : FileProvider(R.xml.paths) {
//    companion object {
//        fun getImageUri(context: Context): ImageFile {
//            val directory = File(context.cacheDir, "images")
//            directory.mkdirs()
//            val file = File.createTempFile(
//                "selected_image_",
//                ".jpg",
//                directory
//            )
//            Log.d("here", "getImageUri file: $file")
//            //val authority = context.packageName + ".fileprovider"
//            val authority = "albayed.moamin.fileprovider"
//            Log.d("here", "getImageUri getUriForFile: ${getUriForFile(
//                context,
//                authority,
//                file,
//            )}")
//            return ImageFile(
//                file, getUriForFile(
//                    context,
//                    authority,
//                    file,
//                )
//            )
//        }
//    }
    companion object {
        fun getImageUri(context: Context, fileName: String): ImageFile {
            val directory = File(context.filesDir, "images")
            directory.mkdirs()
            val file = File(directory, "image_${fileName}.jpg")
            val authority = "albayed.moamin.fileprovider"
            return ImageFile(file, getUriForFile(context, authority, file))
        }
    }

}