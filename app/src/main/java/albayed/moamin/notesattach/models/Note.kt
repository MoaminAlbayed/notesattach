package albayed.moamin.notesattach.models

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
    var imagesCount: Int = 0,
    var videosCount: Int = 0,
    var audioClipsCount: Int = 0,
    var locationsCount: Int = 0,
    var alarmsCount: Int = 0,

    )
