package com.example.lab9.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Definicja Typografii (Material 3)
val Typography = Typography(
    // Używane do tytułów w Card (MovieRow)
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold, // Pogrubienie dla tytułu
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Używane do nagłówków (np. w TopAppBar)
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Domyślny styl tekstu (bodyMedium)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    /* Inne style, jeśli są potrzebne:
    bodyLarge = defaultTextStyle.copy(...),
    labelSmall = defaultTextStyle.copy(...)
    */
)