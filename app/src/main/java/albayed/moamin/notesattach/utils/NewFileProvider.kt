package albayed.moamin.notesattach.utils

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.models.FileInfo
import albayed.moamin.notesattach.models.FileTypes
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.time.Instant
import java.util.*

class NewFileProvider: FileProvider(R.xml.paths){
    companion object {
        fun getFileUri(context: Context, fileTypes: FileTypes): FileInfo {
            val fileName: String
            val folderName: String
            when (fileTypes){
                FileTypes.ImageFile -> {
                    fileName = "image_" + fileDateFormatter(
                        Date.from(
                        Instant.now()).time) + ".jpg"
                    folderName = "images"
                }
                FileTypes.VideoFile -> {
                    fileName = "video_" + fileDateFormatter(
                        Date.from(
                            Instant.now()).time) + ".mp4"
                    folderName = "videos"
                }
                FileTypes.AudioFile -> {
                    fileName = "audioClip_" + fileDateFormatter(
                        Date.from(
                            Instant.now()).time) + ".mp3"//todo confirm mp3 works
                    folderName = "audioClips"
                }
            }
            val directory = File(context.filesDir, folderName)
            directory.mkdirs()
            val file = File(directory, fileName)
            val authority = "albayed.moamin.fileprovider"
            return FileInfo(file, getUriForFile(context, authority, file))
        }
    }
}