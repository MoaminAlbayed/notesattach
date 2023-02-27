package albayed.moamin.composenotesattach.utils

import androidx.room.TypeConverter
import java.io.File

class FileConverter {
    @TypeConverter
    fun FileToString(file: File): String = file.absolutePath

    @TypeConverter
    fun StringToFile(string: String): File = File(string)
}