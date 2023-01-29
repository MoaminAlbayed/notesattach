package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.repository.NoteRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesScreenViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {
    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images = _images.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.getAllImages().collect(){
                _images.value = it
            }
        }

    }

    fun createImage (image: Image) = viewModelScope.launch { noteRepository.createImage(image) }
    fun deleteImage (image: Image) = viewModelScope.launch { noteRepository.deleteImage(image) }

}