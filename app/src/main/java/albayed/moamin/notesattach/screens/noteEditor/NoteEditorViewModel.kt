package albayed.moamin.notesattach.screens.noteEditor

import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.repository.NoteRepository
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel  @Inject constructor(private val noteRepository: NoteRepository): ViewModel(){
//    private val _note = MutableStateFlow<Note>(Note(title = "", content = ""))
//    val note = _note.asStateFlow()
//
//    init {
//        viewModelScope.launch(Dispatchers.IO){
//            noteRepository.getNoteById(noteId).
//            _note = noteRepository.getNoteById(id)
//        }
//    }



    fun createNote(note: Note){
        viewModelScope.launch (Dispatchers.IO) { noteRepository.createNote(note) }
    }
    suspend fun getNoteById(id: String) = noteRepository.getNoteById(id)

    fun updateNote (note: Note){
        viewModelScope.launch((Dispatchers.IO)){ noteRepository.updateNote(note)}
    }
}