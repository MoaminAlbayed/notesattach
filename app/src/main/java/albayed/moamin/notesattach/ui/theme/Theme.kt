package albayed.moamin.notesattach.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
//    primary = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200,
    primary = Color.LightGray,
    onPrimary = Color.Black,
    primaryVariant = Purple700,
    secondary = Teal200,
)

private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200
//    primary = Color.Black,
//    onPrimary = Color.White,
//    primaryVariant = Purple700,
//    secondary = Teal200
    primary = primary,
    onPrimary = onPrimary,
    background = background,
    surface = surface,
    onSurface = onSurface

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun NotesAttachTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
//        color = MaterialTheme.colors.surface
      color = Color (0xff3D0107)
    )
//    systemUiController.setStatusBarColor(
////        color = MaterialTheme.colors.surface
//        color = Color (0xff1A659E)
//    )
    systemUiController.setNavigationBarColor(
        color = Color.Black
    )

    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}