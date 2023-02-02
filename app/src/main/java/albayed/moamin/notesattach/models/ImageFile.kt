package albayed.moamin.notesattach.models

import android.net.Uri
import java.io.File

data class ImageFile(
    val file: File,
    val uri: Uri
)
