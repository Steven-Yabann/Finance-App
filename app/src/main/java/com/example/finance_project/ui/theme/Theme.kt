package com.example.finance_project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Light Color Scheme - White and Green Theme
 * This is the primary theme for the Finance App
 */
private val LightColors = lightColorScheme(
    // Primary - Brand Green
    primary = BrandGreen,                    // Main green color for buttons, FABs, etc.
    onPrimary = White,                       // Text/icons on primary color
    primaryContainer = BrandGreenSurface,    // Light green container
    onPrimaryContainer = BrandGreenDark,     // Text on primary container

    // Secondary - Neutral Gray
    secondary = DarkGray,                    // Secondary actions
    onSecondary = White,                     // Text on secondary
    secondaryContainer = LightGray,          // Secondary container
    onSecondaryContainer = Black,            // Text on secondary container

    // Tertiary - Info Blue (for variety)
    tertiary = Info,                         // Tertiary actions
    onTertiary = White,                      // Text on tertiary
    tertiaryContainer = Color(0xFFDCEEFF),   // Light blue container
    onTertiaryContainer = Color(0xFF1E3A8A), // Dark blue text

    // Error - Red
    error = Error,                           // Error states
    onError = White,                         // Text on error
    errorContainer = Color(0xFFFEE2E2),      // Light red container
    onErrorContainer = Color(0xFF991B1B),    // Dark red text

    // Background - White
    background = White,                      // Main background
    onBackground = Black,                    // Text on background

    // Surface - White with variants
    surface = White,                         // Cards, sheets, menus
    onSurface = Black,                       // Text on surface
    surfaceVariant = SurfaceVariant,         // Variant surface (light gray)
    onSurfaceVariant = TextSecondary,        // Text on surface variant

    // Outline - Borders
    outline = BorderLight,                   // Borders and dividers
    outlineVariant = BorderMedium,           // Variant borders

    // Surface Tint
    surfaceTint = BrandGreen,                // Tint for elevated surfaces

    // Inverse Colors (for snackbars, etc.)
    inverseSurface = Black,                  // Inverse surface
    inverseOnSurface = White,                // Text on inverse surface
    inversePrimary = BrandGreenLight,        // Inverse primary

    // Scrim (for modals)
    scrim = Color(0x80000000)                // Semi-transparent black
)

/**
 * Dark Color Scheme - Optional Dark Mode
 * Uses green accents on dark backgrounds
 */
private val DarkColors = darkColorScheme(
    // Primary - Brand Green (slightly lighter for dark mode)
    primary = BrandGreenLight,               // Lighter green for visibility
    onPrimary = Black,                       // Dark text on green
    primaryContainer = BrandGreenDark,       // Dark green container
    onPrimaryContainer = BrandGreenSurface,  // Light green text

    // Secondary - Light Gray
    secondary = Gray,                        // Secondary actions
    onSecondary = Black,                     // Text on secondary
    secondaryContainer = DarkGray,           // Secondary container
    onSecondaryContainer = LightGray,        // Text on secondary container

    // Tertiary - Light Blue
    tertiary = Color(0xFF60A5FA),            // Light blue for dark mode
    onTertiary = Black,                      // Text on tertiary
    tertiaryContainer = Color(0xFF1E3A8A),   // Dark blue container
    onTertiaryContainer = Color(0xFFDCEEFF), // Light blue text

    // Error - Light Red
    error = Color(0xFFFCA5A5),               // Light red for visibility
    onError = Black,                         // Text on error
    errorContainer = Color(0xFF991B1B),      // Dark red container
    onErrorContainer = Color(0xFFFEE2E2),    // Light red text

    // Background - Dark
    background = Color(0xFF0F172A),          // Dark background
    onBackground = White,                    // Text on background

    // Surface - Dark with variants
    surface = Color(0xFF1E293B),             // Dark surface
    onSurface = White,                       // Text on surface
    surfaceVariant = Color(0xFF334155),      // Lighter dark surface
    onSurfaceVariant = Color(0xFFCBD5E1),    // Light text on dark

    // Outline - Borders
    outline = Color(0xFF475569),             // Dark borders
    outlineVariant = Color(0xFF334155),      // Variant borders

    // Surface Tint
    surfaceTint = BrandGreenLight,           // Light green tint

    // Inverse Colors
    inverseSurface = White,                  // Light surface
    inverseOnSurface = Black,                // Dark text
    inversePrimary = BrandGreen,             // Standard green

    // Scrim
    scrim = Color(0xB3000000)                // Darker scrim for dark mode
)

/**
 * Main Theme Composable
 *
 * @param darkTheme Whether to use dark theme (defaults to system setting)
 * @param dynamicColor Whether to use dynamic theming (Material You)
 * @param content The content to theme
 */
@Composable
fun Finance_ProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,  // Set to false to always use custom colors
    content: @Composable () -> Unit
) {
    // For this app, we want to force light theme with white and green
    // If you want to support dark mode, change this to: if (darkTheme) DarkColors else LightColors
    val colorScheme = LightColors  // Always use light theme

    // If you want to support system dark mode:
    // val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Preview Theme for Compose Previews
 * Always uses light theme for consistency
 */
@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    Finance_ProjectTheme(darkTheme = false, content = content)
}
