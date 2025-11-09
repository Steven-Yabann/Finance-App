package com.example.finance_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.finance_project.R
import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.FirebaseAuthManager
import com.example.finance_project.ui.screens.LoginScreen

@Composable
fun ProfileScreen() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Profile Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionTitle("Account")
            SettingItem("Profile", "Edit your profile", Icons.Default.Person)
            SettingItem("Learning Progress", "Manage your email", Icons.Default.Email)

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Preferences")
            SettingItem("Theme", "Choose your app theme", Icons.Default.WbSunny)
            SettingItem("Notifications", "Manage your notification preferences", Icons.Default.Notifications)
            SettingItem("Language", "Select your preferred language", Icons.Default.Language)

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Security")
            SettingItem("Password", "Change your password", Icons.Default.Lock)
            SettingItem("Delete Your Account", "Everything about your account will be deleted", Icons.Default.Delete, contentColor = Color.Red, isDelete = true)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                    val result = FirebaseAuthManager.logout()
                    if (result.isSuccess) {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Text("Log Out", color = Color.Black)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingItem(title: String, subtitle: String, icon: ImageVector, contentColor: Color = Color.Unspecified, isDelete: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDelete) Color.Red.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (contentColor != Color.Unspecified) contentColor else LocalContentColor.current
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!isDelete) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Finance_ProjectTheme {
        ProfileScreen()
    }
}
