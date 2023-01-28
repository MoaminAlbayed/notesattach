package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.repository.NoteRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesScreenViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

}