package com.example.finance_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
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
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Account Section
            item {
                SectionHeader("Account")
            }
            item {
                ProfileSettingCard(
                    title = "Edit Profile",
                    subtitle = "Update your personal information",
                    icon = Icons.Default.Person,
                    onClick = {
                        // Navigate to edit profile screen
                        // For now, we'll just show a simple action
                    }
                )
            }
            item {
                ProfileSettingCard(
                    title = "Learning Progress",
                    subtitle = "Track your learning journey",
                    icon = Icons.Default.TrendingUp,
                    onClick = {
                        // Navigate to progress screen
                        try {
                            navController.navigate("progress")
                        } catch (e: Exception) {
                            // If progress route fails, create a simple progress view
                            // For now, we'll navigate to home as fallback
                            try {
                                navController.navigate("home")
                            } catch (fallbackError: Exception) {
                                // Stay on current screen if all else fails
                            }
                        }
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Preferences Section
            item {
                SectionHeader("Preferences")
            }
            item {
                ProfileSettingCard(
                    title = "Theme",
                    subtitle = "Choose your app theme",
                    icon = Icons.Default.Palette,
                    onClick = { /* TODO: Implement theme selection */ }
                )
            }
            item {
                ProfileSettingCard(
                    title = "Notifications",
                    subtitle = "Manage your notification preferences",
                    icon = Icons.Default.Notifications,
                    onClick = { /* TODO: Implement notification settings */ }
                )
            }
            item {
                ProfileSettingCard(
                    title = "Language",
                    subtitle = "Select your preferred language",
                    icon = Icons.Default.Language,
                    onClick = { /* TODO: Implement language selection */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Security Section
            item {
                SectionHeader("Security")
            }
            item {
                ProfileSettingCard(
                    title = "Change Password",
                    subtitle = "Update your account password",
                    icon = Icons.Default.Lock,
                    onClick = { /* TODO: Implement password change */ }
                )
            }
            item {
                ProfileSettingCard(
                    title = "Delete Account",
                    subtitle = "Permanently delete your account",
                    icon = Icons.Default.Delete,
                    onClick = { /* TODO: Implement account deletion */ },
                    isDestructive = true
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Logout Button
            item {
                Button(
                    onClick = {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun ProfileSettingCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDestructive) 
                Color.Red.copy(alpha = 0.05f) 
            else 
                MaterialTheme.colorScheme.surfaceVariant
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
                        color = if (isDestructive) 
                            Color.Red.copy(alpha = 0.1f) 
                        else 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDestructive) Color.Red else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDestructive) Color.Red else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = if (isDestructive) 
                        Color.Red.copy(alpha = 0.7f) 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (!isDestructive) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Finance_ProjectTheme {
        ProfileScreen(navController = rememberNavController())
    }
}