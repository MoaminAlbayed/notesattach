package albayed.moamin.notesattach.utils

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter (){
    @TypeConverter
    fun UriToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun StringToUri(string: String): Uri = Uri.parse(string)
}