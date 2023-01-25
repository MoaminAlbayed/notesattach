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
    var content: String
)
