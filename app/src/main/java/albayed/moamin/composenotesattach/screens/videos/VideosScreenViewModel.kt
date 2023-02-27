package albayed.moamin.composenotesattach.screens.videos

import albayed.moamin.composenotesattach.models.Video
import albayed.moamin.composenotesattach.repository.NoteRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideosScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    private val _videosCount = MutableStateFlow(0)
    val videos = _videos.asStateFlow()
    val videosCount = _videosCount.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllVideosByNoteId(state.get<String>("noteId")!!).collect(){
                _videos.value = it
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.getVideosCount(state.get<String>("noteId")!!).collect(){
                _videosCount.value = it
            }
        }
    }

    fun createVideo(video: Video) = viewModelScope.launch { noteRepository.createVideo(video) }
    fun deleteVideo(video: Video) = viewModelScope.launch { noteRepository.deleteVideo(video) }

    fun updateVideosCount(videosCount: Int, noteId: String) =
        viewModelScope.launch { noteRepository.updateVideosCount(videosCount, noteId) }

}