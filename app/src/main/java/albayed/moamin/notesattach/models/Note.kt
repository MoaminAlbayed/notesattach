package albayed.moamin.notesattach.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String,
    val date: Date = Date.from(Instant.now()),
    var content: String,
    //todo add counters for attachments
    var imagesCount: Int = 0,
    var videosCount: Int = 0,
    var voiceClipsCount: Int = 0,
    var locationsCount: Int = 0,
    var alarmsCount: Int = 0,

)
