package albayed.moamin.notesattach.repository

import albayed.moamin.notesattach.data.NoteDatabaseDao
import albayed.moamin.notesattach.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao){
    fun getAllNotes (): Flow<List<Note>> = noteDatabaseDao.getAllNotes().flowOn(Dispatchers.IO).conflate()
    suspend fun createNote(note: Note) = noteDatabaseDao.createNote(note)
    suspend fun updateNote(note: Note) = noteDatabaseDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDatabaseDao.deleteNote(note)
}