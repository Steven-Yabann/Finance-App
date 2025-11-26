package com.example.finance_project.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.*
import com.example.finance_project.R
import com.example.finance_project.ui.viewmodel.LearnViewModel

// ----------------------
//  Minimal Color Palette
// ----------------------

private val FinanceGreen = Color(0xFF10B981)
private val TechBlue = Color(0xFF3B82F6)
private val GoldYellow = Color(0xFFF59E0B)
private val PurpleViolet = Color(0xFF8B5CF6)
private val OrangeRed = Color(0xFFEF4444)
private val TealCyan = Color(0xFF06B6D4)

private val BackgroundLight = Color(0xFFF8FAFC)
private val TextPrimary = Color(0xFF1E293B)
private val TextSecondary = Color(0xFF64748B)

private val CompleteGreen = Color(0xFF10B981)

// ----------------------
//  MAIN SCREEN
// ----------------------

@Composable
fun LearnScreen(
    navController: NavController,
    viewModel: LearnViewModel = viewModel()
) {
    val context = LocalContext.current
    val topics = viewModel.topics
    val isLoading = topics.isEmpty()

    LaunchedEffect(Unit) {
        viewModel.loadTopics(context)
    }

    Scaffold(
        topBar = { TopBar() },
        containerColor = BackgroundLight
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Continue learning",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Explore Topics",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
            }

            // Loading state
            if (isLoading) {
                items(3) {
//                    ShimmerLearnCard(modifier = Modifier.fillMaxWidth())
                }
            } else {
                itemsIndexed(topics) { index, topic ->
                    LearnCard(
                        title = topic.title,
                        description = topic.subtitle,
                        color = getTopicColor(index),
                        isCompleted = topic.isCompleted,
                        onClick = { navController.navigate("topicDetail/${topic.id}") }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}


@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Learn",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
    }
}

// ----------------------
//  TOPIC CARD (Minimal)
// ----------------------

@Composable
fun LearnCard(
    title: String,
    description: String,
    color: Color,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp) // Flat & minimal
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_book),
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}


private fun getTopicColor(index: Int): Color {
    val colors = listOf(
        FinanceGreen, TechBlue, GoldYellow,
        PurpleViolet, TealCyan, OrangeRed
    )
    return colors[index % colors.size]
}

// ----------------------
//  PREVIEW
// ----------------------

@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    LearnScreen(navController = rememberNavController())
}
