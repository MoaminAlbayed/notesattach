package albayed.moamin.composenotesattach.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "alarms_table")
data class Alarm(
    @PrimaryKey
    val alarmId: UUID = UUID.randomUUID(),
    val noteId: UUID,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val requestCode: Int
)
