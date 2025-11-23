package com.example.finance_project.data

import androidx.compose.ui.graphics.Color

data class ProgressItem(
    val title: String,
    val subtitle: String,
    val iconId: Int,
    val badge: String? = null,
    val badgeColor: Color = Color.Green,
    val progressText: String? = null
)

val progressData = listOf(
    ProgressItem(
        title = "Market Trends",
        subtitle = "Check live market data",
        iconId = 0,
        badge = "Updated Now"
    ),
    ProgressItem(
        title = "Progress Report",
        subtitle = "View achievements",
        iconId = 1,
        badge = "1,250 XP"
    ),
    ProgressItem(
        title = "Modules",
        subtitle = "50% Complete",
        iconId = 2,
        progressText = "2/4"
    ),
    ProgressItem(
        title = "Learning Days",
        subtitle = "Since 15/01/2024",
        iconId = 3,
        progressText = "590"
    )
)
