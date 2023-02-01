package albayed.moamin.notesattach.repository

import albayed.moamin.notesattach.data.NoteDatabaseDao
import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao){
    fun getAllNotes (): Flow<List<Note>> = noteDatabaseDao.getAllNotes().flowOn(Dispatchers.IO).conflate()
    suspend fun getNoteById(id: String) = noteDatabaseDao.getNoteById(id)
    suspend fun createNote(note: Note) = noteDatabaseDao.createNote(note)
    suspend fun updateNote(note: Note) = noteDatabaseDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDatabaseDao.deleteNote(note)

    fun getAllImagesByNoteId(noteId: String) : Flow<List<Image>> = noteDatabaseDao.getAllImagesByNoteId(noteId).flowOn(Dispatchers.IO).conflate()
    suspend fun createImage(image: Image) = noteDatabaseDao.createImage(image)
    suspend fun deleteImage(image: Image) = noteDatabaseDao.deleteImage(image)


    suspend fun updateImagesCount(imagesCount: Int, noteId: String) =
        noteDatabaseDao.updateImagesCount(imagesCount, noteId)
    suspend fun updateVideosCount(videosCount: Int, noteId: String) =
        noteDatabaseDao.updateVideosCount(videosCount, noteId)
    suspend fun updateVoiceClipsCount(voiceClipsCount: Int, noteId: String) =
        noteDatabaseDao.updateVoiceClipsCount(voiceClipsCount, noteId)
    suspend fun updateLocationsCount(locationsCount: Int, noteId: String) =
        noteDatabaseDao.updateLocationsCount(locationsCount, noteId)
    suspend fun updateAlarmsCount(alarmsCount: Int, noteId: String) =
        noteDatabaseDao.updateAlarmsCount(alarmsCount, noteId)

    suspend fun getImagesCount (noteId: String) = noteDatabaseDao.getImagesCount(noteId)
    suspend fun getVideosCount (noteId: String) = noteDatabaseDao.getVideosCount(noteId)
    suspend fun getVoiceClipsCount (noteId: String) = noteDatabaseDao.getVoiceClipsCount(noteId)
    suspend fun getLocationsCount (noteId: String) = noteDatabaseDao.getLocationsCount(noteId)
    suspend fun getAlarmsCount (noteId: String) = noteDatabaseDao.getAlarmsCount(noteId)


}