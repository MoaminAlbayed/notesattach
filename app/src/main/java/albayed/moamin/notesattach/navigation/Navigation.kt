package albayed.moamin.notesattach.navigation

import albayed.moamin.notesattach.screens.alarms.AlarmsScreen
import albayed.moamin.notesattach.screens.mainscreen.MainScreen
import albayed.moamin.notesattach.screens.audioClips.AudioClipsScreen
import albayed.moamin.notesattach.screens.audioClips.RecordAudioScreen
import albayed.moamin.notesattach.screens.images.ImagesScreen
import albayed.moamin.notesattach.screens.locations.LocationsScreen
import albayed.moamin.notesattach.screens.locations.MapScreen
import albayed.moamin.notesattach.screens.noteEditor.NoteEditor
import albayed.moamin.notesattach.screens.videos.VideosScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        composable(route = Screens.MainScreen.name,
        ) {
            MainScreen(navController = navController)
        }

        composable(route = Screens.NoteEditor.name + "/{isNewNote}/{isFromNotification}/{noteId}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "myapp://notesattach/{isNewNote}/{isFromNotification}/{noteId}"}
            ),
            arguments = listOf(
                navArgument(name = "isNewNote") {
                    type = NavType.BoolType
                },
                navArgument(name = "noteId") {
                    type = NavType.StringType
                },
                navArgument(name = "isFromNotification") {
                    type = NavType.BoolType
                }

            )) { navBackStack ->
            val isNewNote = navBackStack.arguments?.getBoolean("isNewNote")
            val isFromNotification = navBackStack.arguments?.getBoolean("isFromNotification")
            val noteId = navBackStack.arguments?.getString("noteId")
            if (isNewNote != null) {
                NoteEditor(navController = navController, isNewNote = isNewNote, isFromNotification = isFromNotification!!, noteId = noteId)
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
        composable(route = Screens.VideosScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            VideosScreen(navController = navController, noteId = noteId.toString())
        }
        composable(route = Screens.AudioClipsScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            AudioClipsScreen(navController = navController, noteId = noteId.toString())
        }
        composable(route = Screens.RecordAudioScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) {navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            RecordAudioScreen(navController = navController, noteId = noteId.toString())
        }
        composable(route = Screens.LocationsScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            LocationsScreen(navController = navController, noteId = noteId.toString())
        }
        composable(route = Screens.MapScreen.name + "/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            MapScreen(navController = navController, noteId = noteId.toString())
        }
        composable(route = Screens.AlarmsScreen.name + "/{noteId}/?{noteTitle}",
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.StringType
                },
                navArgument(name = "noteTitle"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { navBackStack ->
            val noteId = navBackStack.arguments?.getString("noteId")
            val noteTitle = navBackStack.arguments?.getString("noteTitle")
            AlarmsScreen(navController = navController, noteId = noteId.toString(), noteTitle = noteTitle.toString())
        }
    }
}