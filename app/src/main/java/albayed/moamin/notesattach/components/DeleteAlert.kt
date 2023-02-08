package albayed.moamin.notesattach.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

@Composable
fun DeleteAlert(
    isOpenDeleteDialog: MutableState<Boolean>,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit,
    title: String,
    text: String,
) {
    AlertDialog(onDismissRequest = { isOpenDeleteDialog.value = false },
        buttons = {
            TextButton(onClick = {
               onClickYes.invoke()
            }) {
                Text(text = "Yes", style = MaterialTheme.typography.button)
            }
            TextButton(onClick = { onClickNo.invoke()}) {
                Text(text = "No", style = MaterialTheme.typography.button)
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