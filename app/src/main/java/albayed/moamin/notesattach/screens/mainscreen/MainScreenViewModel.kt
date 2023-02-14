package albayed.moamin.notesattach.screens.mainscreen

import albayed.moamin.notesattach.models.*
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
        if (note.videosCount > 0)
            deleteAllVideos(note)
        if (note.audioClipsCount > 0)
            deleteAllAudioClips(note)
        if (note.locationsCount > 0)
            deleteAllLocations(note)
        if (note.alarmsCount > 0)
            deleteAllAlarms(note)
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

    private fun deleteAllVideos(note: Note) {
        val videos = MutableStateFlow<List<Video>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllVideosByNoteId(note.id.toString()).collect() {
                videos.value = it
            }
            videos.value.forEach { video ->
                noteRepository.deleteVideo(video)
            }
        }
    }

    private fun deleteAllAudioClips(note: Note){
        val audioClips = MutableStateFlow<List<AudioClip>>(emptyList())
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.getAllAudioClipsByNoteId(note.id.toString()).collect(){
                audioClips.value = it
            }
            audioClips.value.forEach { audioClip ->
                noteRepository.deleteAudioClip(audioClip)
            }
        }
    }

    private fun deleteAllLocations(note: Note){
        val locations = MutableStateFlow<List<Location>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllLocationsByNoteId(note.id.toString()).collect(){
                locations.value = it
            }
            locations.value.forEach { location ->
                noteRepository.deleteLocation(location)
            }
        }
    }

    private fun deleteAllAlarms(note: Note){
        val alarms = MutableStateFlow<List<Alarm>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllAlarmsByNoteId(note.id.toString()).collect(){
                alarms.value = it
            }
            alarms.value.forEach { alarm ->
                noteRepository.deleteAlarm(alarm)
            }
        }
    }
}