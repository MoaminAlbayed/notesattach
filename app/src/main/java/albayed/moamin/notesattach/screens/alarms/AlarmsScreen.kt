package albayed.moamin.notesattach.screens.alarms

import albayed.moamin.notesattach.R
import albayed.moamin.notesattach.components.ConfirmMessage
import albayed.moamin.notesattach.components.FloatingButton
import albayed.moamin.notesattach.components.TopBar
import albayed.moamin.notesattach.models.Alarm
import albayed.moamin.notesattach.models.Location
import albayed.moamin.notesattach.navigation.Screens
import albayed.moamin.notesattach.utils.BackPressHandler
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AlarmsScreen(
    navController: NavController,
    noteId: String,
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

    val calendar = Calendar.getInstance()
    var nowCalendar: Calendar
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
        TimePickerDialog(context, { _, hourOfDay: Int, minute: Int ->
            time = "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
            setCalendar.set(Calendar.YEAR, setYear)
            setCalendar.set(Calendar.MONTH, setMonth)
            setCalendar.set(Calendar.DAY_OF_MONTH, setDay)
            setCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            setCalendar.set(Calendar.MINUTE, minute)
            nowCalendar = Calendar.getInstance()
            if (setCalendar <= nowCalendar) {
                Toast.makeText(context, "Reminders have to be in the future!", Toast.LENGTH_LONG)
                    .show()
            } else {
//                val requestCode = Random.nextInt()
                val requestCode = 0
                val channelId = Random.nextInt()
                val alarmIntent = Intent(context, AlarmReceiver::class.java)
                alarmIntent.putExtra("content", "Test Content")
                alarmIntent.putExtra("channelId", channelId)
                alarmIntent.putExtra("requestCode", requestCode)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                Log.d("here", "AlarmsScreen set: ${setCalendar.timeInMillis}")
                Log.d("here", "AlarmsScreen now: ${nowCalendar.timeInMillis}")
                alarmManager.set(AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, pendingIntent)
            }
        }, hour, minute, true)

    val datePicker =
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = "${dayOfMonth.toString().padStart(2, '0')}/${
                (month + 1).toString().padStart(2, '0')
            }/$year"
            setDay = dayOfMonth
            setMonth = month
            setYear = year
            timePicker.show()
        }, year, month, day)


    Scaffold(
        topBar = {
            TopBar(
                screen = Screens.AlarmsScreen,
                navController = navController,
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

            }
        }
    }
    if (isOpenDeleteDialog.value) {
        ConfirmMessage(
            isOpenDialog = isOpenDeleteDialog,
            onClickYes = {
                alarmsToDelete.forEach { alarm ->
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
            text = "Are you sure you want to delete ${alarmsToDelete.size} location(s)?"
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