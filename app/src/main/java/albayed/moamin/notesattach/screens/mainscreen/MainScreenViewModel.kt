package albayed.moamin.notesattach.screens.mainscreen

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.repository.NoteRepository
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {
    private var _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList = _notesList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNotes().collect() {
                _notesList.value = it

            }
        }
    }


    fun deleteNote(note: Note) = viewModelScope.launch {
        if (note.imagesCount > 0)
            deleteAllImages(note)
        noteRepository.deleteNote(note)
    }//TODO loop through all attachments to delete


    private fun deleteAllImages(note: Note) {
        val images = MutableStateFlow<List<Image>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllImagesByNoteId(note.id.toString()).collect() {
                images.value = it
            }
            images.value.forEach { image ->
                noteRepository.deleteImage(image)
            }
        }

    }
}