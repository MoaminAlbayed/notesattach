package albayed.moamin.composenotesattach.utils

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter (){
    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun stringToUri(string: String): Uri = Uri.parse(string)
}