package albayed.moamin.notesattach.navigation

import albayed.moamin.notesattach.screens.MainScreen
import albayed.moamin.notesattach.screens.newnote.NewNote
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        composable(route = Screens.MainScreen.name) {
              MainScreen(navController = navController)
        }

        composable(route = Screens.NewNote.name) {
            NewNote(navController = navController)
        }
    }
}