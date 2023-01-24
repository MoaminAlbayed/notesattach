package albayed.moamin.notesattach.screens.newnote

import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.repository.NoteRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel(){
    fun createNote(note: Note){
        viewModelScope.launch (Dispatchers.IO) { noteRepository.createNote(note) }
    }
}