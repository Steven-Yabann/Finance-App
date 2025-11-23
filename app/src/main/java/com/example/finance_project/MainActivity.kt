package com.example.finance_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance_project.ui.market.MarketScreen
import com.example.finance_project.ui.screens.LoginScreen
import com.example.finance_project.ui.screens.ProgressScreen
import com.example.finance_project.ui.screens.SignupScreen
import com.example.finance_project.ui.viewmodel.CryptoNewsViewModel
import com.example.finance_project.ui.viewmodel.getFormattedTime
import com.example.finance_project.ui.viewmodel.getMainCategory
import com.example.finance_project.ui.viewmodel.getSentiment
import com.example.finance_project.data.model.CryptoNewsArticle


import com.example.finance_project.ui.theme.Finance_ProjectTheme
import com.example.finance_project.ui.screens.LearnScreen
import com.google.firebase.FirebaseApp
import com.example.finance_project.ui.screens.ProfileScreen
import com.example.finance_project.ui.screens.TopicDetailScreen


// --- Data Classes for UI Content ---
// Real API integration - no dummy data needed

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
            if (isLoggedIn) {
                AppBottomBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { 
                val viewModel: CryptoNewsViewModel = viewModel()
                CryptoNewsScreen(viewModel = viewModel, navController = navController) 
            }
            composable("article_detail/{articleIndex}") { backStackEntry ->
                val articleIndex = backStackEntry.arguments?.getString("articleIndex")?.toIntOrNull() ?: 0
                val viewModel: CryptoNewsViewModel = viewModel()
                val article = viewModel.newsState.value.news.getOrNull(articleIndex)
                if (article != null) {
                    ArticleDetailScreen(article = article, navController = navController)
                } else {
                    // Article not found, navigate back
                    LaunchedEffect(Unit) {
                        navController.navigateUp()
                    }
                }
            }
            composable("learn") { LearnScreen(navController) }
            composable("markets") { MarketScreen() }
            composable("profile") { ProfileScreen(navController) }
            composable("topicDetail/{topicId}") { backStackEntry ->
                val topicId = backStackEntry.arguments?.getString("topicId")
                TopicDetailScreen(topicId = topicId)
            }
            composable("progress") {
                ProgressScreen(navController = navController)
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
fun CryptoNewsScreen(modifier: Modifier = Modifier, viewModel: CryptoNewsViewModel, navController: NavHostController) {
    val newsState = viewModel.newsState.value
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Title Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Crypto News",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Latest cryptocurrency news • Tap to read more",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                
                // Refresh button
                if (!newsState.isLoading) {
                    IconButton(
                        onClick = { viewModel.retryLoading() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Loading state
        if (newsState.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading latest crypto news...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        
        // Error state
        newsState.error?.let { error ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Error",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Failed to load news",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.retryLoading() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // News list - Limited to 10 cards for better performance
        newsState.news.take(10).forEachIndexed { index, article ->
            item {
                RealCryptoNewsCard(
                    article = article,
                    onClick = {
                        navController.navigate("article_detail/$index")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Empty state
        if (!newsState.isLoading && newsState.error == null && newsState.news.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "No news",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No news available",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Pull to refresh or try again later",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// --- Reusable Card Composable for Real API Data ---

@Composable
fun RealCryptoNewsCard(article: CryptoNewsArticle, onClick: () -> Unit = {}) {
    val sentiment = article.getSentiment()
    val category = article.getMainCategory()
    val timeAgo = article.getFormattedTime()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with category and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF2196F3).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Sentiment indicator
                Box(
                    modifier = Modifier
                        .background(
                            color = sentiment.second.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = sentiment.first,
                        style = MaterialTheme.typography.labelSmall,
                        color = sentiment.second,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Body preview (first 150 characters)
            Text(
                text = if (article.body.length > 150) {
                    article.body.take(150) + "..."
                } else {
                    article.body
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Footer with source and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.sourceInfo.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
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

// --- Article Detail Screen ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(article: CryptoNewsArticle, navController: NavHostController) {
    val sentiment = article.getSentiment()
    val category = article.getMainCategory()
    val timeAgo = article.getFormattedTime()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Article Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                // Header with category and sentiment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF2196F3).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(
                                color = sentiment.second.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = sentiment.first,
                            style = MaterialTheme.typography.labelMedium,
                            color = sentiment.second,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Source and time info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Source: ${article.sourceInfo.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = timeAgo,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Full article body
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Article Content",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = article.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black.copy(alpha = 0.8f),
                            lineHeight = 24.sp
                        )
                    }
                }
                
                // Tags if available
                if (article.tags.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = "Tags",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val tags = article.tags.split("|").filter { it.isNotBlank() }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.take(5).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag.trim(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
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
