package com.example.finance_project.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.ui.theme.rememberThemeState
import com.example.finance_project.ui.theme.availableThemeColors
import com.example.finance_project.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Use global theme state instead of local state
    val themeState = rememberThemeState()
    var showColorPicker by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeState.getCurrentPrimaryColor().copy(alpha = 0.1f),
                    titleContentColor = if (themeState.isDarkMode) Color.White else themeState.getCurrentPrimaryColor()
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Account Section
            item {
                SectionHeader("Account", themeState.getCurrentPrimaryColor(), themeState.isDarkMode)
            }
            item {
                ProfileSettingCard(
                    title = "Learning Progress",
                    subtitle = "Track your learning journey",
                    icon = Icons.Default.TrendingUp,
                    onClick = {
                        try {
                            navController.navigate("progress")
                        } catch (e: Exception) {
                            try {
                                navController.navigate("home")
                            } catch (fallbackError: Exception) {
                                // Stay on current screen if all else fails
                            }
                        }
                    },
                    themeColor = themeState.getCurrentPrimaryColor(),
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Theme Customization Section
            item {
                SectionHeader("Appearance", themeState.getCurrentPrimaryColor(), themeState.isDarkMode)
            }
            
            // Dark/Light Mode Toggle
            item {
                ThemeToggleCard(
                    title = "Dark Mode",
                    subtitle = if (themeState.isDarkMode) "Dark theme enabled" else "Light theme enabled",
                    icon = if (themeState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                    isEnabled = themeState.isDarkMode,
                    onToggle = { themeState.toggleDarkMode() },
                    themeColor = themeState.getCurrentPrimaryColor(),
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Theme Color Picker
            item {
                ProfileSettingCard(
                    title = "Theme Color",
                    subtitle = "Choose your favorite color: ${themeState.selectedThemeColor.name}",
                    icon = Icons.Default.Palette,
                    onClick = { showColorPicker = !showColorPicker },
                    themeColor = themeState.getCurrentPrimaryColor(),
                    isDarkMode = themeState.isDarkMode
                )
            }
            
            // Color Picker
            if (showColorPicker) {
                item {
                    ColorPickerSection(
                        selectedColor = themeState.selectedThemeColor,
                        onColorSelected = { themeState.setThemeColor(it) },
                        isDarkMode = themeState.isDarkMode
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Security Section
            item {
                SectionHeader("Security", themeState.getCurrentPrimaryColor(), themeState.isDarkMode)
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Logout Button
            item {
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (themeState.isDarkMode) Color(0xFF2A2A2A) else Color(0xFFF5F5F5),
                        contentColor = if (themeState.isDarkMode) Color.White else Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.Bold,
                    color = if (themeState.isDarkMode) Color.White else Color.Black
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out of your account?",
                    color = if (themeState.isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        try {
                            val result = FirebaseAuthManager.logout()
                            if (result.isSuccess) {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        } catch (e: Exception) {
                            // Handle logout error
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = themeState.getCurrentPrimaryColor()
                    )
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (themeState.isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Gray
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = if (themeState.isDarkMode) Color(0xFF1E1E1E) else Color.White,
            titleContentColor = if (themeState.isDarkMode) Color.White else Color.Black,
            textContentColor = if (themeState.isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
        )
    }
    
    // Delete Account Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Account",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete your account? This action cannot be undone and all your data will be lost.",
                    color = if (themeState.isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        // TODO: Implement actual account deletion
                        // This would typically involve calling Firebase Auth delete method
                        // and cleaning up user data from the database
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Delete Account")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (themeState.isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Gray
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = if (themeState.isDarkMode) Color(0xFF1E1E1E) else Color.White,
            titleContentColor = Color.Red,
            textContentColor = if (themeState.isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun SectionHeader(title: String, themeColor: Color, isDarkMode: Boolean) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = if (isDarkMode) themeColor.copy(alpha = 0.9f) else themeColor,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun ProfileSettingCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    themeColor: Color,
    isDarkMode: Boolean,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isDestructive -> Color.Red.copy(alpha = 0.05f)
                isDarkMode -> Color(0xFF1E1E1E)
                else -> Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkMode) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = when {
                            isDestructive -> Color.Red.copy(alpha = 0.1f)
                            else -> themeColor.copy(alpha = 0.1f)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDestructive) Color.Red else themeColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        isDestructive -> Color.Red
                        isDarkMode -> Color.White
                        else -> Color.Black
                    }
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = when {
                        isDestructive -> Color.Red.copy(alpha = 0.7f)
                        isDarkMode -> Color.White.copy(alpha = 0.7f)
                        else -> Color.Gray
                    }
                )
            }
            
            if (!isDestructive) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeToggleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    themeColor: Color,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkMode) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = themeColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = themeColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) Color.White else Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Gray
                )
            }
            
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = themeColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun ColorPickerSection(
    selectedColor: com.example.finance_project.ui.theme.ThemeColor,
    onColorSelected: (com.example.finance_project.ui.theme.ThemeColor) -> Unit,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkMode) 0.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Choose Theme Color",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableThemeColors) { themeColor ->
                    ColorOption(
                        themeColor = themeColor,
                        isSelected = selectedColor == themeColor,
                        onClick = { onColorSelected(themeColor) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorOption(
    themeColor: com.example.finance_project.ui.theme.ThemeColor,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(themeColor.color)
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) Color.White else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = themeColor.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) themeColor.color else Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Finance_ProjectTheme {
        ProfileScreen(navController = rememberNavController())
    }
}