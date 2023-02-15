package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.models.Alarm
import albayed.moamin.notesattach.utils.alarmDateFormatter
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    alarm: Alarm,
    nowCalendar: Calendar,
    isDeleteMode: MutableState<Boolean>,
    isNewDeleteProcess: Boolean,
    onClick: (Alarm) -> Unit,
    checkedDelete: (MutableState<Boolean>) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isSelected = remember {
        mutableStateOf(false)
    }
    if (isNewDeleteProcess) {
        isSelected.value = false
    }

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, alarm.year)
    calendar.set(Calendar.MONTH, alarm.month)
    calendar.set(Calendar.DAY_OF_MONTH, alarm.day)
    calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
    calendar.set(Calendar.MINUTE, alarm.minute)

    Log.d("here", "AlarmCard nowCalendar: ${nowCalendar.time}")
    Log.d("here", "AlarmCard calendar: ${calendar.time}")
    if (calendar > nowCalendar){
        Log.d("here", "AlarmCard : newer")
    }else {
        Log.d("here", "AlarmCard : older")
    }

    val textColor = if (calendar > nowCalendar) MaterialTheme.colors.primary else Color.Gray

    ConstraintLayout(
        modifier = modifier
            .padding(5.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isDeleteMode.value) {
                            checkedDelete(isSelected)
                        } else {
                            onClick(alarm)
                        }
                    },
                    onLongPress = {
                        isDeleteMode.value = true
                        checkedDelete(isSelected)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }
    ) {
        val checkRef = createRef()
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            elevation = 5.dp
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                text = alarmDateFormatter(alarm),
                color = textColor,
                fontSize = 23.sp,
                textAlign = TextAlign.Center
            )
        }
        if (isDeleteMode.value) {
            CircleCheckbox(modifier = Modifier.constrainAs(checkRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }, selected = isSelected.value) {
                checkedDelete(isSelected)
            }
        }
    }
}