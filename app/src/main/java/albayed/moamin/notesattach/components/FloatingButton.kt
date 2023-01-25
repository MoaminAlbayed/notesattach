package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.navigation.Screens
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun FloatingButton(screen: Screens, navController: NavController) {
    when (screen){
        Screens.MainScreen -> {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.NoteEditor.name+"/${true}/${null}") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create New Note")
            }
        }
        Screens.NoteEditor -> {
            //TODO
        }
    }
}