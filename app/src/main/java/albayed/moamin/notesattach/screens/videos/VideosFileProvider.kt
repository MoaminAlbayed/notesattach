package albayed.moamin.notesattach.screens.videos

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.VideoFile
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File

class VideosFileProvider : FileProvider(R.xml.paths) {
    companion object {
        fun getVideoUri(context: Context, fileNumber: Int): VideoFile {
            val directory = File(context.filesDir, "videos")
            directory.mkdirs()
            val file = File(directory, "video_${fileNumber}.mp4")
            val authority = "albayed.moamin.fileprovider"
            return VideoFile(file, getUriForFile(context, authority, file))
        }
    }
}