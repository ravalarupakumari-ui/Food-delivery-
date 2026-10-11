package com.example.ui.theme

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
    primary = CraveonPrimary,
    onPrimary = Color.White,
    primaryContainer = CraveonPrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = CraveonSecondary,
    onSecondary = Color.Black,
    secondaryContainer = CraveonDarkSurfaceVariant,
    onSecondaryContainer = Color.White,
    background = CraveonDarkBg,
    onBackground = Color(0xFFF0F0F2),
    surface = CraveonDarkSurface,
    onSurface = Color(0xFFF0F0F2),
    surfaceVariant = CraveonDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0B4BC),
    outline = CraveonDarkBorder,
)

private val LightColorScheme = lightColorScheme(
    primary = CraveonPrimary,
    onPrimary = Color.White,
    primaryContainer = CraveonPrimaryLight,
    onPrimaryContainer = CraveonPrimaryVariant,
    secondary = CraveonSecondary,
    onSecondary = Color.White,
    secondaryContainer = CraveonSecondaryLight,
    onSecondaryContainer = CraveonDark,
    background = CraveonBgLight,
    onBackground = CraveonDark,
    surface = CraveonSurfaceLight,
    onSurface = CraveonDark,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = CraveonMuted,
    outline = CraveonLightBorder,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand Craveon orange consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
