package albayed.moamin.composenotesattach.screens.noteEditor

import albayed.moamin.composenotesattach.models.Note
import albayed.moamin.composenotesattach.repository.NoteRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel  @Inject constructor(private val noteRepository: NoteRepository): ViewModel(){

    fun createNote(note: Note){
        viewModelScope.launch (Dispatchers.IO) { noteRepository.createNote(note) }
    }
    suspend fun getNoteById(id: String) = noteRepository.getNoteById(id)

    fun updateNote (note: Note){
        viewModelScope.launch((Dispatchers.IO)){ noteRepository.updateNote(note)}
    }

    fun deleteNote (note: Note){
        viewModelScope.launch((Dispatchers.IO)){ noteRepository.deleteNote(note)}
    }
}