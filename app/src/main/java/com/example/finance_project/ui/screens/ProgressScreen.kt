package com.example.finance_project.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.ui.theme.rememberThemeState
import kotlin.random.Random

// Color Palette for Progress
private val GoldColor = Color(0xFFFFD700)
private val SilverColor = Color(0xFFC0C0C0)
private val BronzeColor = Color(0xFFCD7F32)
private val SuccessGreen = Color(0xFF4CAF50)
private val WarningOrange = Color(0xFFFF9800)
private val InfoBlue = Color(0xFF2196F3)
private val PurpleAccent = Color(0xFF9C27B0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavController) {
    val themeState = rememberThemeState()
    val primaryColor = themeState.getCurrentPrimaryColor()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Journey",
                        fontWeight = FontWeight.Bold,
                        color = if (themeState.isDarkMode) Color.White else primaryColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (themeState.isDarkMode) Color.White else primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (themeState.isDarkMode) Color(0xFF1E1E1E) else Color.White
                )
            )
        },
        containerColor = if (themeState.isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // User Level & XP Section
            item {
                UserLevelCard(
                    level = 12,
                    currentXP = 2450,
                    nextLevelXP = 3000,
                    userName = "Finance Explorer",
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Weekly Progress Streak
            item {
                StreakCard(
                    currentStreak = 7,
                    longestStreak = 28,
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Skill Categories Progress
            item {
                SkillProgressSection(
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Achievements & Badges
            item {
                AchievementsSection(
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Recent Activity
            item {
                RecentActivitySection(
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Statistics Overview
            item {
                StatsOverviewSection(
                    primaryColor = primaryColor,
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun UserLevelCard(
    level: Int,
    currentXP: Int,
    nextLevelXP: Int,
    userName: String,
    primaryColor: Color,
    isDarkMode: Boolean
) {
    val progress = currentXP.toFloat() / nextLevelXP.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1500, easing = EaseOutBack),
        label = "xp_progress"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.1f),
                            primaryColor.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                        Text(
                            text = "Level $level Explorer",
                            fontSize = 16.sp,
                            color = primaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(primaryColor, primaryColor.copy(alpha = 0.7f))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$currentXP XP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                        Text(
                            text = "$nextLevelXP XP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.2f),
                                RoundedCornerShape(6.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .height(12.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(primaryColor, primaryColor.copy(alpha = 0.8f))
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${nextLevelXP - currentXP} XP to next level",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun StreakCard(
    currentStreak: Int,
    longestStreak: Int,
    primaryColor: Color,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Learning Streak",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black
                )
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = WarningOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StreakStat(
                    title = "Current",
                    value = "$currentStreak days",
                    color = SuccessGreen,
                    isDarkMode = isDarkMode
                )
                StreakStat(
                    title = "Longest",
                    value = "$longestStreak days",
                    color = primaryColor,
                    isDarkMode = isDarkMode
                )
                StreakStat(
                    title = "This Week",
                    value = "5/7 days",
                    color = InfoBlue,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
private fun StreakStat(
    title: String,
    value: String,
    color: Color,
    isDarkMode: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Gray
        )
    }
}

@Composable
private fun SkillProgressSection(
    primaryColor: Color,
    isDarkMode: Boolean
) {
    val skills = listOf(
        SkillData("Investment Basics", 0.85f, SuccessGreen, Icons.Default.TrendingUp),
        SkillData("Risk Management", 0.65f, WarningOrange, Icons.Default.Shield),
        SkillData("Portfolio Theory", 0.45f, InfoBlue, Icons.Default.PieChart),
        SkillData("Market Analysis", 0.30f, PurpleAccent, Icons.Default.Analytics),
        SkillData("Crypto Knowledge", 0.70f, primaryColor, Icons.Default.CurrencyBitcoin)
    )
    
    Column {
        Text(
            text = "Skill Progress",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        skills.forEach { skill ->
            SkillProgressItem(
                skill = skill,
                isDarkMode = isDarkMode
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SkillProgressItem(
    skill: SkillData,
    isDarkMode: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = skill.progress,
        animationSpec = tween(durationMillis = 1000),
        label = "skill_progress"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        skill.color.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = skill.icon,
                    contentDescription = skill.name,
                    tint = skill.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = skill.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                    Text(
                        text = "${(skill.progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        color = skill.color,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(
                            Color.Gray.copy(alpha = 0.2f),
                            RoundedCornerShape(3.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(6.dp)
                            .background(
                                skill.color,
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementsSection(
    primaryColor: Color,
    isDarkMode: Boolean
) {
    val achievements = listOf(
        AchievementData("First Steps", "Complete your first lesson", Icons.Default.School, true, BronzeColor),
        AchievementData("Week Warrior", "7-day learning streak", Icons.Default.LocalFireDepartment, true, SilverColor),
        AchievementData("Knowledge Seeker", "Complete 10 lessons", Icons.Default.MenuBook, true, GoldColor),
        AchievementData("Expert Analyst", "Master market analysis", Icons.Default.Analytics, false, Color.Gray),
        AchievementData("Portfolio Master", "Create your first portfolio", Icons.Default.AccountBalance, false, Color.Gray),
        AchievementData("Crypto Pioneer", "Learn about blockchain", Icons.Default.CurrencyBitcoin, false, Color.Gray)
    )
    
    Column {
        Text(
            text = "Achievements",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementBadge(
                    achievement = achievement,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
private fun AchievementBadge(
    achievement: AchievementData,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { /* Handle achievement tap */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (achievement.isUnlocked) 6.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        achievement.color.copy(alpha = if (achievement.isUnlocked) 1f else 0.3f),
                        CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (achievement.isUnlocked) achievement.color else Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = achievement.name,
                    tint = if (achievement.isUnlocked) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = achievement.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (achievement.isUnlocked) {
                    if (isDarkMode) Color.White else Color.Black
                } else Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = achievement.description,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun RecentActivitySection(
    primaryColor: Color,
    isDarkMode: Boolean
) {
    val activities = listOf(
        ActivityData("Completed: Investment Basics", "2 hours ago", Icons.Default.CheckCircle, SuccessGreen),
        ActivityData("Started: Risk Management", "1 day ago", Icons.Default.PlayArrow, primaryColor),
        ActivityData("Quiz passed: Portfolio Theory", "3 days ago", Icons.Default.Quiz, InfoBlue),
        ActivityData("Achievement unlocked: Week Warrior", "5 days ago", Icons.Default.EmojiEvents, GoldColor)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Recent Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            activities.forEach { activity ->
                ActivityItem(
                    activity = activity,
                    isDarkMode = isDarkMode
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ActivityItem(
    activity: ActivityData,
    isDarkMode: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    activity.color.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = activity.icon,
                contentDescription = activity.title,
                tint = activity.color,
                modifier = Modifier.size(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) Color.White else Color.Black
            )
            Text(
                text = activity.time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StatsOverviewSection(
    primaryColor: Color,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Total Time",
            value = "24h 30m",
            icon = Icons.Default.AccessTime,
            color = primaryColor,
            isDarkMode = isDarkMode,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Lessons",
            value = "18/25",
            icon = Icons.Default.School,
            color = SuccessGreen,
            isDarkMode = isDarkMode,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Accuracy",
            value = "87%",
            icon = Icons.Default.CheckCircle,
            color = InfoBlue,
            isDarkMode = isDarkMode,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color.Black
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// Data Classes
private data class SkillData(
    val name: String,
    val progress: Float,
    val color: Color,
    val icon: ImageVector
)

private data class AchievementData(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val color: Color
)

private data class ActivityData(
    val title: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)
