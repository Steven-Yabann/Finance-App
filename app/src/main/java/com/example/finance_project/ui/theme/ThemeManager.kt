package com.example.finance_project.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// Theme colors available for selection
data class ThemeColor(
    val name: String,
    val color: Color,
    val lightVariant: Color,
    val darkVariant: Color
)

val availableThemeColors = listOf(
    ThemeColor("Blue", Color(0xFF2196F3), Color(0xFF64B5F6), Color(0xFF1976D2)),
    ThemeColor("Green", Color(0xFF4CAF50), Color(0xFF81C784), Color(0xFF388E3C)),
    ThemeColor("Purple", Color(0xFF9C27B0), Color(0xFFBA68C8), Color(0xFF7B1FA2)),
    ThemeColor("Red", Color(0xFFF44336), Color(0xFFE57373), Color(0xFFD32F2F)),
    ThemeColor("Orange", Color(0xFFFF9800), Color(0xFFFFB74D), Color(0xFFF57C00)),
    ThemeColor("Teal", Color(0xFF009688), Color(0xFF4DB6AC), Color(0xFF00796B)),
    ThemeColor("Indigo", Color(0xFF3F51B5), Color(0xFF7986CB), Color(0xFF303F9F)),
    ThemeColor("Pink", Color(0xFFE91E63), Color(0xFFF06292), Color(0xFFC2185B)),
    ThemeColor("Cyan", Color(0xFF00BCD4), Color(0xFF4DD0E1), Color(0xFF0097A7)),
    ThemeColor("Lime", Color(0xFF8BC34A), Color(0xFFAED581), Color(0xFF689F38))
)

// Global theme state
class ThemeState {
    var isDarkMode by mutableStateOf(false)
    var selectedThemeColor by mutableStateOf(availableThemeColors[0])
    
    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }
    
    fun setThemeColor(color: ThemeColor) {
        selectedThemeColor = color
    }
    
    fun getCurrentPrimaryColor(): Color {
        return if (isDarkMode) {
            selectedThemeColor.darkVariant
        } else {
            selectedThemeColor.color
        }
    }
    
    fun getCurrentSecondaryColor(): Color {
        return if (isDarkMode) {
            selectedThemeColor.color
        } else {
            selectedThemeColor.lightVariant
        }
    }
}

// Global instance
val GlobalThemeState = ThemeState()

// Composable to provide theme state
@Composable
fun rememberThemeState(): ThemeState {
    return remember { GlobalThemeState }
}