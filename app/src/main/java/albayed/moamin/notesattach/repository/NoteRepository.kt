package albayed.moamin.notesattach.repository

import albayed.moamin.notesattach.database.NoteDatabaseDao
import albayed.moamin.notesattach.models.AudioClip
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.models.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao) {
    fun getAllNotes(): Flow<List<Note>> =
        noteDatabaseDao.getAllNotes().flowOn(Dispatchers.IO).conflate()

    suspend fun getNoteById(id: String) = noteDatabaseDao.getNoteById(id)
    suspend fun createNote(note: Note) = noteDatabaseDao.createNote(note)
    suspend fun updateNote(note: Note) = noteDatabaseDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDatabaseDao.deleteNote(note)

    fun getAllImagesByNoteId(noteId: String): Flow<List<Image>> =
        noteDatabaseDao.getAllImagesByNoteId(noteId).flowOn(Dispatchers.IO).conflate()

    suspend fun createImage(image: Image) = noteDatabaseDao.createImage(image)
    suspend fun deleteImage(image: Image) {
        image.file.delete()
        noteDatabaseDao.deleteImage(image)
    }

    fun getAllVideosByNoteId(noteId: String): Flow<List<Video>> =
        noteDatabaseDao.getAllVideosByNoteId(noteId).flowOn(Dispatchers.IO).conflate()

    suspend fun createVideo(video: Video) = noteDatabaseDao.createVideo(video)
    suspend fun deleteVideo(video: Video) {
        video.file.delete()
        noteDatabaseDao.deleteVideo(video)
    }

    fun getAllAudioClipsByNoteId(noteId: String): Flow<List<AudioClip>> =
        noteDatabaseDao.getAllAudioClipsByNoteId(noteId).flowOn(Dispatchers.IO).conflate()
    suspend fun createAudioClip (audioClip: AudioClip) = noteDatabaseDao.createAudioClip(audioClip)
    suspend fun deleteAudioClip (audioClip: AudioClip) {
        audioClip.file.delete()
        noteDatabaseDao.deleteAudioClip(audioClip)
    }

    suspend fun updateImagesCount(imagesCount: Int, noteId: String) =
        noteDatabaseDao.updateImagesCount(imagesCount, noteId)

    suspend fun updateVideosCount(videosCount: Int, noteId: String) =
        noteDatabaseDao.updateVideosCount(videosCount, noteId)

    suspend fun updateAudioClipsCount(audioClipsCount: Int, noteId: String) =
        noteDatabaseDao.updateAudioClipsCount(audioClipsCount, noteId)

    suspend fun updateLocationsCount(locationsCount: Int, noteId: String) =
        noteDatabaseDao.updateLocationsCount(locationsCount, noteId)

    suspend fun updateAlarmsCount(alarmsCount: Int, noteId: String) =
        noteDatabaseDao.updateAlarmsCount(alarmsCount, noteId)

    suspend fun getImagesCount(noteId: String) = noteDatabaseDao.getImagesCount(noteId)
    suspend fun getVideosCount(noteId: String) = noteDatabaseDao.getVideosCount(noteId)
    suspend fun getAudioClipsCount(noteId: String) = noteDatabaseDao.getAudioClipsCount(noteId)
    suspend fun getLocationsCount(noteId: String) = noteDatabaseDao.getLocationsCount(noteId)
    suspend fun getAlarmsCount(noteId: String) = noteDatabaseDao.getAlarmsCount(noteId)


}