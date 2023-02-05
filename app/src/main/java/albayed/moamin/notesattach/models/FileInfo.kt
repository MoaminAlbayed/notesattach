package albayed.moamin.notesattach.models

import android.net.Uri
import java.io.File

data class FileInfo(
    val file: File,
    val uri: Uri
)
