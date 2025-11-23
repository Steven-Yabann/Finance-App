package com.example.finance_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import com.example.finance_project.ui.market.MarketScreen
import com.example.finance_project.ui.screens.SignupScreen
import com.example.finance_project.ui.screens.LoginScreen


import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.ui.screens.LearnScreen
import com.google.firebase.FirebaseApp
import com.example.finance_project.ui.screens.ProfileScreen
import com.example.finance_project.ui.screens.TopicDetailScreen


// --- Data Classes for UI Content ---

data class ProgressItem(
    val title: String,
    val subtitle: String,
    val iconId: Int, // Placeholder for icon resource or content description
    val badge: String? = null,
    val badgeColor: Color = Color.Green,
    val progressText: String? = null
)


// Dummy data for the screen
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

// --- MainActivity: Entry point of the app ---

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidx.compose.foundation.ComposeFoundationFlags.isNonComposedClickableEnabled = false

        FirebaseApp.initializeApp(this)
        Log.d("FirebaseInit", "Firebase initialized: ${FirebaseApp.getApps(this).isNotEmpty()}")
        setContent {
            // Use your app's theme here
            Finance_ProjectTheme { // Changed from YourAppTheme
                MainScreen()
            }
        }
    }
}

// --- Main Composable: Defines the overall screen structure ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val isLoggedIn = FirebaseAuthManager.isUserLoggedIn()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo(0)
            }
        } else {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
    }

    Scaffold(
        bottomBar = {

                AppBottomBar(navController)

        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreenContent(items = progressData) }
            composable("learn") { LearnScreen(navController) }
            composable("markets") { MarketScreen() }
            composable("profile") { ProfileScreen() }
            composable("topicDetail/{topicId}") { backStackEntry ->
                val topicId = backStackEntry.arguments?.getString("topicId")
                TopicDetailScreen(topicId = topicId)
            }

            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToSignup = { navController.navigate("signup") }
                )
            }

            composable("signup") {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
        }
    }
}




// --- Top Bar Composable ---

@Composable
fun AppTopBar() {
    // We are using a simple Row instead of TopAppBar to match the simple design
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for the Profile Icon (Circular Avatar)
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(50))
        ) {
            // Replace with actual Icon/Image
            Text("P", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
    }
}

// --- Bottom Navigation Bar Composable ---

@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", "home", R.drawable.ic_home),
        BottomNavItem("Learn", "learn", R.drawable.ic_school),
        BottomNavItem("Markets", "markets", R.drawable.ic_market),
        BottomNavItem("Profile", "profile", R.drawable.ic_person)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else Color.Gray
                    )
                }
            )
        }
    }
}



data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: Int
)


// --- Main Content Area Composable ---

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier, items: List<ProgressItem>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Title Section
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Track your learning journey and celebrate achievements",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // List of Cards
        items.forEach { item ->
            item {
                ProgressCard(item = item)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// --- Reusable Card Composable ---

@Composable
fun ProgressCard(item: ProgressItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: Icon and Text
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon Placeholder (Light purple circle with placeholder text)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEDE7F6), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Replace with actual icon based on item.iconId
                    Text(
                        item.title.first().toString(),
                        color = Color(0xFF673AB7),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Title and Subtitle
                Column {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = item.subtitle.split("\n").first(), // Subtitle
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Right Side: Progress Text or Badge
            Column(horizontalAlignment = Alignment.End) {
                if (item.badge != null) {
                    Badge(text = item.badge, color = if (item.badge.contains("XP")) Color(0xFF4CAF50) else Color.LightGray)
                } else if (item.progressText != null) {
                    // Progress text (e.g., "2/4" or "590")
                    Text(
                        text = item.progressText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// --- Helper Badge Composable ---

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (color == Color.LightGray) Color.Black else Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- Preview Function ---

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Finance_ProjectTheme {
        MainScreen()
    }
}
