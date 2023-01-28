package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.R
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ImagesFileProvider: FileProvider(R.xml.paths) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            //val authority = context.packageName + ".fileprovider"
            val authority = "albayed.moamin.fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}