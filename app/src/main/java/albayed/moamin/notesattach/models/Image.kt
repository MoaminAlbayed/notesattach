package albayed.moamin.notesattach.models

import android.net.Uri
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "images_table")
data class Image(
    val id: UUID,
    val uri: Uri,
)
