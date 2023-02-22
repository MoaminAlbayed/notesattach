package albayed.moamin.notesattach.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun ConfirmMessage(
    isOpenDialog: MutableState<Boolean>,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit,
    title: String,
    text: String,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.9f),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        onDismissRequest = { isOpenDialog.value = false },
        buttons = {
            TextButton(onClick = {
               onClickYes.invoke()
            }) {
                Text(text = "Yes", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
            }
            TextButton(onClick = { onClickNo.invoke()}) {
                Text(text = "No", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
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