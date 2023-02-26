package albayed.moamin.notesattach.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ConfirmMessage(
    isOpenDialog: MutableState<Boolean> = mutableStateOf(false),
    onClickYes: () -> Unit = {},
    onClickNo: () -> Unit = {},
    title: String = "Title",
    text: String = "Are You sure?",
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.9f),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(10.dp),
        contentColor = MaterialTheme.colors.onPrimary,
        onDismissRequest = { isOpenDialog.value = false },
        buttons = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onClickYes.invoke()
                }) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 13.dp),
                    text = "Yes",
                    style = MaterialTheme.typography.button,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClickNo.invoke() }) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 13.dp),
                    text = "No",
                    style = MaterialTheme.typography.button,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        },
        title = {
            Text(text = title, style = MaterialTheme.typography.h6)
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.body1
            )
        }
    )
}