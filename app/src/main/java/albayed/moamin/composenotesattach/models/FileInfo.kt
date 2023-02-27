package albayed.moamin.composenotesattach.models

import android.net.Uri
import java.io.File

data class FileInfo(
    val file: File,
    val uri: Uri
)
