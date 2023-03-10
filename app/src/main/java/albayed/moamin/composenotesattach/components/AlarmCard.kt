package albayed.moamin.composenotesattach.components

import albayed.moamin.composenotesattach.models.Alarm
import albayed.moamin.composenotesattach.utils.alarmDateFormatter
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
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
    onClick: (Alarm) -> Unit = {},
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


    val textColor = if (calendar > nowCalendar) MaterialTheme.colors.onSurface else Color.DarkGray

    ConstraintLayout(//Constrained Layout can be used to position en element on top of another element
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
        val checkRef = createRef()//reference used for positioning the check marker on the card
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            backgroundColor = MaterialTheme.colors.surface,
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