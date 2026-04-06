package ai.mlxdroid.selfmassage.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = Primary,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Secondary,
    onSecondaryContainer = Color.White,
    tertiary = Accent,
    onTertiary = Foreground,
    tertiaryContainer = Accent,
    onTertiaryContainer = Foreground,
    background = Background,
    onBackground = Foreground,
    surface = Color.White,
    onSurface = Foreground,
    surfaceVariant = CardFill,
    onSurfaceVariant = MutedForeground,
    error = Destructive,
    onError = Color.White,
    outline = Color(0x1A000000)
)

@Composable
fun SelfMassageTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
