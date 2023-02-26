package albayed.moamin.notesattach.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.util.UUID

@Entity(tableName = "videos_table")
data class Video(
    @PrimaryKey
    val videoId: UUID = UUID.randomUUID(),
    val noteId: UUID,
    val uri: Uri,
    val file: File,
    val length: String
)
