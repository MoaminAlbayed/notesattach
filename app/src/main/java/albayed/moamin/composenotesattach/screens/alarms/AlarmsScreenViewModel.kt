package albayed.moamin.composenotesattach.screens.alarms

import albayed.moamin.composenotesattach.models.Alarm
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
class AlarmsScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val state: SavedStateHandle//used to read navigation arguments
) : ViewModel() {
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    private val _alarmsCount = MutableStateFlow(0)
    val alarms = _alarms.asStateFlow()
    val alarmsCount = _alarmsCount.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.getAllAlarmsByNoteId(state.get<String>("noteId")!!).collect(){
                _alarms.value = it
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            noteRepository.getAlarmsCount(state.get<String>("noteId")!!).collect(){
                _alarmsCount.value = it
            }
        }
    }

    fun createAlarm (alarm: Alarm) = viewModelScope.launch { noteRepository.createAlarm(alarm) }
    fun deleteAlarm(alarm: Alarm) = viewModelScope.launch { noteRepository.deleteAlarm(alarm) }

    fun updateAlarmsCount(alarmsCount: Int, noteId: String) =
        viewModelScope.launch { noteRepository.updateAlarmsCount(alarmsCount, noteId) }
}