package albayed.moamin.notesattach.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "images_table")
data class Image(
    @PrimaryKey
    val imageId:UUID =UUID.randomUUID(),
    val noteId: UUID,
    val uri: Uri,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val thumbnail: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (!thumbnail.contentEquals(other.thumbnail)) return false

        return true
    }

    override fun hashCode(): Int {
        return thumbnail.contentHashCode()
    }
}
