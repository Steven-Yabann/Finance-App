package com.example.finance_project.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.finance_project.ui.viewmodel.LearnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topicId: String?,
    viewModel: LearnViewModel = viewModel()
) {
    val context = LocalContext.current
    val topics by viewModel.topics.collectAsState()

    LaunchedEffect(Unit) {
        if (topics.isEmpty()) viewModel.loadTopics(context)
    }

    val topic = topics.find { it.id == topicId }

    if (topic == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Topic not found", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic.title, fontWeight = FontWeight.Bold) },
                navigationIcon = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(topic.imageUrl),
                contentDescription = topic.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = topic.subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sections
            topic.sections.forEach { section ->
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = section.content,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            topic.videoUrl?.let { video ->
                Text(
                    text = "Watch Video: $video",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
