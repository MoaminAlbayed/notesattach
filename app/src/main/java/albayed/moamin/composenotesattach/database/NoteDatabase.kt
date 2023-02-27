package albayed.moamin.composenotesattach.database

import albayed.moamin.composenotesattach.models.*
import androidx.room.Database
import androidx.room.TypeConverters
import albayed.moamin.composenotesattach.utils.DateConverter
import albayed.moamin.composenotesattach.utils.FileConverter
import albayed.moamin.composenotesattach.utils.UUIDConverter
import albayed.moamin.composenotesattach.utils.UriConverter
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Image::class, Video::class, AudioClip::class, Location::class, Alarm::class], version = 14, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class, UriConverter::class, FileConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDatabaseDao
}