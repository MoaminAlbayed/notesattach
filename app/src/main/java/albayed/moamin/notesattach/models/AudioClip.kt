package albayed.moamin.notesattach.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.util.UUID

@Entity (tableName = "audio_table")
data class AudioClip(
    @PrimaryKey
    val audioId: UUID = UUID.randomUUID(),
    val noteId: String,
    val duration: Int,
    val uri: Uri,
    val file: File
)
