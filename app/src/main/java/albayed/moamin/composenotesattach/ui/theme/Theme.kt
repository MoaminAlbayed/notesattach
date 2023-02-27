package albayed.moamin.composenotesattach.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = primary,
    onPrimary = onPrimary,
    background = background,
    surface = surface,
    onSurface = onSurface
)



@Composable
fun NotesAttachTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
      color = Color (0xff3D0107)
    )

    systemUiController.setNavigationBarColor(
        color = Color.Black
    )

    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}