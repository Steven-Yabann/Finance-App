package com.example.finance_project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Create dynamic color schemes based on theme selection
 */
@Composable
fun createLightColorScheme(primaryColor: Color, secondaryColor: Color) = lightColorScheme(
    // Primary - User selected theme color
    primary = primaryColor,
    onPrimary = White,
    primaryContainer = secondaryColor,
    onPrimaryContainer = primaryColor,

    // Secondary - Neutral Gray
    secondary = DarkGray,
    onSecondary = White,
    secondaryContainer = LightGray,
    onSecondaryContainer = Black,

    // Tertiary - Accent based on primary
    tertiary = primaryColor.copy(alpha = 0.7f),
    onTertiary = White,
    tertiaryContainer = Color(0xFFDCEEFF),
    onTertiaryContainer = Color(0xFF1E3A8A),

    // Error - Red
    error = Error,
    onError = White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B),

    // Background - White
    background = White,
    onBackground = Black,

    // Surface - White with variants
    surface = White,
    onSurface = Black,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    // Outline - Borders
    outline = BorderLight,
    outlineVariant = BorderMedium,

    // Surface Tint
    surfaceTint = primaryColor,

    // Inverse Colors
    inverseSurface = Black,
    inverseOnSurface = White,
    inversePrimary = secondaryColor,

    // Scrim
    scrim = Color(0x80000000)
)

@Composable
fun createDarkColorScheme(primaryColor: Color, secondaryColor: Color) = darkColorScheme(
    // Primary - User selected theme color (lighter for dark mode)
    primary = secondaryColor,
    onPrimary = Color(0xFF000000),
    primaryContainer = primaryColor,
    onPrimaryContainer = secondaryColor,

    // Secondary - Light Gray
    secondary = Gray,
    onSecondary = Black,
    secondaryContainer = DarkGray,
    onSecondaryContainer = LightGray,

    // Tertiary - Light accent
    tertiary = secondaryColor.copy(alpha = 0.8f),
    onTertiary = Black,
    tertiaryContainer = Color(0xFF1E3A8A),
    onTertiaryContainer = Color(0xFFDCEEFF),

    // Error - Light Red
    error = Color(0xFFFCA5A5),
    onError = Black,
    errorContainer = Color(0xFF991B1B),
    onErrorContainer = Color(0xFFFEE2E2),

    // Background - Dark
    background = Color(0xFF0F172A),
    onBackground = White,

    // Surface - Dark with variants
    surface = Color(0xFF1E293B),
    onSurface = White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),

    // Outline - Borders
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155),

    // Surface Tint
    surfaceTint = secondaryColor,

    // Inverse Colors
    inverseSurface = White,
    inverseOnSurface = Black,
    inversePrimary = primaryColor,

    // Scrim
    scrim = Color(0xB3000000)
)

/**
 * Main Theme Composable with Global Theme State Integration
 *
 * @param content The content to theme
 */
@Composable
fun Finance_ProjectTheme(
    content: @Composable () -> Unit
) {
    val themeState = rememberThemeState()
    
    val primaryColor = themeState.getCurrentPrimaryColor()
    val secondaryColor = themeState.getCurrentSecondaryColor()
    
    val colorScheme = if (themeState.isDarkMode) {
        createDarkColorScheme(primaryColor, secondaryColor)
    } else {
        createLightColorScheme(primaryColor, secondaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Preview Theme for Compose Previews
 * Always uses light theme with default colors for consistency
 */
@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = createLightColorScheme(BrandGreen, BrandGreenLight),
        typography = Typography,
        content = content
    )
}
