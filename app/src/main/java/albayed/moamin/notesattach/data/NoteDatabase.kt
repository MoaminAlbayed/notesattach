package albayed.moamin.notesattach.data

import albayed.moamin.notesattach.models.Note
import androidx.room.Database
import androidx.room.TypeConverters
import albayed.moamin.notesattach.utils.DateConverter
import albayed.moamin.notesattach.utils.UUIDConverter
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}