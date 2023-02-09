package albayed.moamin.notesattach.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.time.Instant
import java.util.*

@Entity (tableName = "audio_table")
data class AudioClip(
    @PrimaryKey
    val audioClipId: UUID = UUID.randomUUID(),
    val noteId: String,
    val duration: Int,
    val uri: Uri,
    val file: File,
    val date: Date = Date.from(Instant.now()),
)
