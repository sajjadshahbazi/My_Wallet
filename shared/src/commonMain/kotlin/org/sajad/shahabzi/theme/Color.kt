package org.sajad.shahabzi.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val LightColors = lightColorScheme(
    primary = Color(0xFF3B5BDB),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF6F7F9),
    onBackground = Color(0xFF101828),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101828),
    onSurfaceVariant = Color(0xFF475467),
    outline = Color(0xFFD0D5DD),
)

internal val DarkColors = darkColorScheme(
    primary = Color(0xFF8DA2FF),
    onPrimary = Color(0xFF0B1B66),
    background = Color(0xFF0B0F17),
    onBackground = Color(0xFFF2F4F7),
    surface = Color(0xFF141A26),
    onSurface = Color(0xFFF2F4F7),
    onSurfaceVariant = Color(0xFF98A2B3),
    outline = Color(0xFF344054),
)