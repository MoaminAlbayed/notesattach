package albayed.moamin.composenotesattach.screens.audioClips

import albayed.moamin.composenotesattach.models.AudioClip
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
class AudioClipsScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _audioClips = MutableStateFlow<List<AudioClip>>(emptyList())
    private val _audioClipsCount = MutableStateFlow(0)
    val audioClips = _audioClips.asStateFlow()
    val audioClipsCount = _audioClipsCount.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllAudioClipsByNoteId(state.get<String>("noteId")!!).collect(){
                _audioClips.value = it
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.getAudioClipsCount(state.get<String>("noteId")!!).collect(){
                _audioClipsCount.value = it
            }
        }
    }

    fun createAudioClip (audioClip: AudioClip) = viewModelScope.launch { noteRepository.createAudioClip(audioClip) }
    fun deleteAudioClip (audioClip: AudioClip) = viewModelScope.launch { noteRepository.deleteAudioClip(audioClip) }
    fun updateAudioClipsCount (audioClipsCount: Int, noteId: String) =
        viewModelScope.launch { noteRepository.updateAudioClipsCount(audioClipsCount, noteId) }

}