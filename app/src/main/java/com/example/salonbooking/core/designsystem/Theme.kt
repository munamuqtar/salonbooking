package com.example.salonbooking.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = BrandSurface,
    primaryContainer = BrandSecondary,
    onPrimaryContainer = BrandPrimaryDark,
    secondary = BrandAccent,
    onSecondary = BrandSurface,
    secondaryContainer = BrandSecondary,
    onSecondaryContainer = BrandText,
    tertiary = BrandAccent,
    onTertiary = BrandSurface,
    background = BrandBackground,
    onBackground = BrandText,
    surface = BrandSurface,
    onSurface = BrandText,
    surfaceVariant = BrandSurfaceVariant,
    onSurfaceVariant = BrandTextSecondary,
    error = BrandError,
    onError = BrandSurface,
    outline = BrandBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryLight,
    onPrimary = BrandText,
    primaryContainer = BrandPrimaryDark,
    onPrimaryContainer = BrandSecondary,
    secondary = BrandAccent,
    onSecondary = BrandText,
    secondaryContainer = BrandPrimaryDark,
    onSecondaryContainer = BrandSecondary,
    background = BrandText,
    onBackground = BrandBackground,
    surface = BrandText,
    onSurface = BrandBackground,
    surfaceVariant = BrandPrimaryDark,
    onSurfaceVariant = BrandSecondary,
    error = BrandError,
    onError = BrandSurface,
    outline = BrandBorder
)

@Composable
fun SalonBookingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
