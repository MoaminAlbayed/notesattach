package albayed.moamin.composenotesattach.screens.locations

import albayed.moamin.composenotesattach.models.Location
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
class LocationsScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    private val _locationsCount = MutableStateFlow(0)
    val location = _locations.asStateFlow()
    val locationsCount = _locationsCount.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.getAllLocationsByNoteId(state.get<String>("noteId")!!).collect(){
                _locations.value = it
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.getLocationsCount(state.get<String>("noteId")!!).collect(){
                _locationsCount.value = it
            }
        }
    }

    fun createLocation (location: Location) = viewModelScope.launch { noteRepository.createLocation(location) }
    fun deleteLocation (location: Location) = viewModelScope.launch { noteRepository.deleteLocation(location) }

    fun updateLocationsCount(locationsCount: Int, noteId: String) =
        viewModelScope.launch { noteRepository.updateLocationsCount(locationsCount, noteId) }
}