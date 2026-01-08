package com.example.lab9.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- SCHEMATY KOLORÓW ---

// Schemat Kolorów dla Trybu Ciemnego
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1C1C), // Ciemne tło
)

// Schemat Kolorów dla Trybu Jasnego
private val LightColorScheme = lightColorScheme(
    primary = Kurs2_Primary,      // Używane dla akcentów, np. TopAppBar
    secondary = Kurs2_Secondary,  // Używane jako drugi akcent, np. BottomAppBar
    tertiary = Kurs2_Tertiary,    // Trzeci kolor akcentu
    background = Kurs2_Background, // Białe tło (dla Scaffold)
    surface = Color.White,        // Powierzchnie (np. Card)
    onPrimary = Color.White,      // Tekst na Primary
    onSecondary = Color.White,    // Tekst na Secondary
    onTertiary = Color.Black,     // Tekst na Tertiary
    onBackground = Color.Black,   // Tekst na tle
    onSurface = Color.Black       // Tekst na powierzchniach
)

// --- GŁÓWNA FUNKCJA MOTYWU ---

@Composable
fun Kurs2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        // Ustawienia paska statusu
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Użycie z Type.kt
        content = content,
        shapes = Shapes
    )
}
