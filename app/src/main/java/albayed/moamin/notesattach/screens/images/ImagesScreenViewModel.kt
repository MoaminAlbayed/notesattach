package albayed.moamin.notesattach.screens.images

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.repository.NoteRepository
import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class ImagesScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    //SavedStateHandle allows reading navigation arguments
    private val _images = MutableStateFlow<List<Image>>(emptyList())
    private val _imagesCount = MutableStateFlow<Int>(0)
    val images = _images.asStateFlow()
    val imagesCount = _imagesCount.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllImagesByNoteId(state.get<String>("noteId")!!).collect() {
                _images.value = it
            }

        }
        viewModelScope.launch ( Dispatchers.IO ){
            noteRepository.getImagesCount(state.get<String>("noteId")!!).collect(){
                _imagesCount.value = it
            }
        }

    }

    fun createImage(image: Image) = viewModelScope.launch { noteRepository.createImage(image) }
    fun deleteImage(image: Image) = viewModelScope.launch { noteRepository.deleteImage(image) }

    fun updateImagesCount(imagesCount: Int, noteId: String) =
        viewModelScope.launch { noteRepository.updateImagesCount(imagesCount, noteId) }


}