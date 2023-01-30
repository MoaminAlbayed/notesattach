package albayed.moamin.notesattach.data

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import androidx.room.Database
import androidx.room.TypeConverters
import albayed.moamin.notesattach.utils.DateConverter
import albayed.moamin.notesattach.utils.UUIDConverter
import albayed.moamin.notesattach.utils.UriConverter
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Image::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class, UriConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}