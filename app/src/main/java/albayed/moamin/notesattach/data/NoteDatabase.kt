package albayed.moamin.notesattach.data

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.models.Video
import androidx.room.Database
import androidx.room.TypeConverters
import albayed.moamin.notesattach.utils.DateConverter
import albayed.moamin.notesattach.utils.FileConverter
import albayed.moamin.notesattach.utils.UUIDConverter
import albayed.moamin.notesattach.utils.UriConverter
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Image::class, Video::class], version = 6, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class, UriConverter::class, FileConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}