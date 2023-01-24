package albayed.moamin.notesattach.utils



import java.text.SimpleDateFormat
import java.util.*

fun dateFormatter(time: Long): String{
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(time))
}