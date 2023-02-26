package albayed.moamin.notesattach.screens.alarms

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.AlarmCard
import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Alarm
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.util.*
import kotlin.random.Random

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnspecifiedImmutableFlag")
@Composable
fun AlarmsScreen(
    navController: NavController,
    noteId: String,
    noteTitle: String,
    viewModel: AlarmsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val alarmsList = viewModel.alarms.collectAsState().value
    val alarmsCount = viewModel.alarmsCount.collectAsState().value

    val isDeleteMode = remember {
        mutableStateOf(false)
    }
    val isOpenDeleteDialog = remember {
        mutableStateOf(false)
    }

    val alarmsToDelete = remember {
        mutableStateListOf<Alarm>()
    }

    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    var alarmIntent = Intent(context, AlarmReceiver::class.java)

    val calendar = Calendar.getInstance()
    var nowCalendar: Calendar
    nowCalendar = Calendar.getInstance()
    calendar.time = Date()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val setCalendar = Calendar.getInstance()
    var setDay = 0
    var setMonth = 0
    var setYear = 0


    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }


    val timePicker =
        TimePickerDialog(context, { _, chosenHourOfDay: Int, chosenMinute: Int ->
            time = "${chosenHourOfDay.toString().padStart(2, '0')}:${chosenMinute.toString().padStart(2, '0')}"
            setCalendar.set(Calendar.YEAR, setYear)
            setCalendar.set(Calendar.MONTH, setMonth)
            setCalendar.set(Calendar.DAY_OF_MONTH, setDay)
            setCalendar.set(Calendar.HOUR_OF_DAY, chosenHourOfDay)
            setCalendar.set(Calendar.MINUTE, chosenMinute)
            nowCalendar = Calendar.getInstance()
            if (setCalendar <= nowCalendar) {
                Toast.makeText(context, "Reminders have to be in the future!", Toast.LENGTH_LONG)
                    .show()
            } else {
                val requestCode = Random.nextInt()
                val channelId = Random.nextInt()
                val content = noteTitle.ifEmpty { "Click to view note" }
                alarmIntent = Intent(context, AlarmReceiver::class.java)
                alarmIntent.putExtra("content", content)
                alarmIntent.putExtra("channelId", channelId)
                alarmIntent.putExtra("requestCode", requestCode)
                alarmIntent.putExtra("noteId", noteId)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, pendingIntent)
                viewModel.createAlarm(
                    Alarm(
                        noteId = UUID.fromString(noteId),
                        year = setYear,
                        month = setMonth,
                        day = setDay,
                        hour = chosenHourOfDay,
                        minute = chosenMinute,
                        requestCode = requestCode
                    )
                )
                viewModel.updateAlarmsCount(alarmsCount = alarmsCount + 1, noteId)
            }
        }, hour, minute, true)

    val datePicker =
        DatePickerDialog(context, { _: DatePicker, chosenYear: Int, chosenMonth: Int, chosenDayOfMonth: Int ->
            date = "${chosenDayOfMonth.toString().padStart(2, '0')}/${
                (chosenMonth + 1).toString().padStart(2, '0')
            }/$chosenYear"
            setDay = chosenDayOfMonth
            setMonth = chosenMonth
            setYear = chosenYear
            timePicker.show()
        }, year, month, day)


    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.AlarmsScreen,
                firstAction = {
                    if (alarmsCount == 0) {
                        Toast.makeText(context, "No Alarms to Delete!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!isDeleteMode.value) {
                        isDeleteMode.value = true
                    } else {
                        if (alarmsToDelete.isNotEmpty()) {
                            isOpenDeleteDialog.value = true
                        } else {
                            isDeleteMode.value = false
                        }
                    }
                }) {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                    alarmsToDelete.clear()
                } else {
                    navController.popBackStack()
                }
            }
        },
        floatingActionButton = {
            FloatingButton(icon = R.drawable.alarm, contentDescription = "Add Alarm Button") {
                if (isDeleteMode.value) {
                    isDeleteMode.value = false
                    alarmsToDelete.clear()
                }
                datePicker.show()
            }
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(alarmsList.asReversed()) { alarm ->
                AlarmCard(
                    alarm = alarm,
                    nowCalendar = nowCalendar,
                    isDeleteMode = isDeleteMode,
                    isNewDeleteProcess = alarmsToDelete.isEmpty(),
                    checkedDelete = { checkedDelete ->
                        if (checkedDelete.value) {
                            checkedDelete.value = !checkedDelete.value
                            alarmsToDelete.remove(alarm)
                        } else {
                            checkedDelete.value = !checkedDelete.value
                            alarmsToDelete.add(alarm)
                        }
                    }
                )
            }
        }
    }
    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                alarmsToDelete.forEach { alarm ->
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        alarm.requestCode,
                        alarmIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.cancel(pendingIntent)
                    viewModel.deleteAlarm(alarm)
                }
                viewModel.updateAlarmsCount(
                    alarmsCount = alarmsCount - alarmsToDelete.size,
                    noteId = noteId
                )
                alarmsToDelete.clear()
                isOpenDeleteDialog.value = false
                isDeleteMode.value = false
            },
            onClickNo = {
                isOpenDeleteDialog.value = false
                alarmsToDelete.clear()
                isDeleteMode.value = false
            },
            title = "Deleting Locations",
            text = "Are you sure you want to delete ${alarmsToDelete.size} reminder(s)?"
        )
    }
    fun backToMainScreen() {
        if (isDeleteMode.value) {
            isDeleteMode.value = false
            alarmsToDelete.clear()
        } else
            navController.popBackStack()
    }
    BackPressHandler() {
        backToMainScreen()
    }
}