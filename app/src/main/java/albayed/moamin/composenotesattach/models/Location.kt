package albayed.moamin.composenotesattach.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity (tableName = "locations_table")
data class Location(
    @PrimaryKey
    val locationId: UUID = UUID.randomUUID(),
    val noteId: UUID,
    val longitude: Double,
    val latitude: Double,
    val description: String
)
