package albayed.moamin.notesattach.navigation

import albayed.moamin.notesattach.screens.MainScreen
import albayed.moamin.notesattach.screens.images.ImagesScreen
import albayed.moamin.notesattach.screens.noteEditor.NoteEditor
import android.icu.text.MessagePattern.ArgType
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        composable(route = Screens.MainScreen.name) {
            MainScreen(navController = navController)
        }

        //composable(route = Screens.NoteEditor.name + "?isNewNote={isNewNote}&noteId={noteId}",
        composable(route = Screens.NoteEditor.name + "/{isNewNote}/{noteId}",
            arguments = listOf(
                navArgument(name = "isNewNote") {
                    type = NavType.BoolType
                },
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }

            )) { navBackStack ->
//            navBackStack.arguments?.getString("noteId")
//            navBackStack.arguments?.get
//                ?.let { NoteEditor(navController = navController, it) }
            val isNewNote = navBackStack.arguments?.getBoolean("isNewNote")
            val noteId = navBackStack.arguments?.getString("noteId")
            if (isNewNote != null) {
                Log.d("isNewNote", "Navigation: $isNewNote")
                NoteEditor(navController = navController, isNewNote = isNewNote, noteId = noteId)
            }

        }
        composable(route = Screens.ImagesScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            ImagesScreen(navController = navController, noteId = noteId.toString())
        }
    }
}