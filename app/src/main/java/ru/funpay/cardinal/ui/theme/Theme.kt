package ru.funpay.cardinal.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CardinalPurple,
    onPrimary = Color.White,
    primaryContainer = CardinalPurpleDark,
    onPrimaryContainer = CardinalPurpleLight,
    secondary = CardinalGold,
    onSecondary = Color.Black,
    secondaryContainer = CardinalGold,
    onSecondaryContainer = CardinalGoldLight,
    tertiary = CardinalSuccess,
    background = CardinalBackground,
    onBackground = CardinalTextPrimary,
    surface = CardinalSurface,
    onSurface = CardinalTextPrimary,
    surfaceVariant = CardinalSurfaceVariant,
    onSurfaceVariant = CardinalTextSecondary,
    error = CardinalError,
    onError = Color.White,
    outline = CardinalTextSecondary
)

@Composable
fun FunPayCardinalTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
