package com.example.finance_project.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance_project.data.model.TopicModel
import com.example.finance_project.data.repository.TopicRepository
import kotlinx.coroutines.launch

class LearnViewModel : ViewModel() {
    var topics by mutableStateOf<List<TopicModel>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    // Keep a copy of all data to filter against
    private var allTopics = listOf<TopicModel>()

    fun loadTopics(context: Context) {
        viewModelScope.launch {
            val repository = TopicRepository(context)
            val data = repository.loadTopics()
            Log.d("LearnViewModel", "Loaded ${data.size} topics")

            allTopics = data

            // Apply search if needed, otherwise show all
            if (searchQuery.isEmpty()) {
                topics = data
            } else {
                searchTopics(searchQuery)
            }
        }
    }

    fun markTopicCompleted(topicId: String?) {
        if (topicId == null) return

        // Simple logic: Create a new list with the modified item
        val updatedList = allTopics.map { topic ->
            if (topic.id == topicId) {
                topic.copy(isCompleted = true)
            } else {
                topic
            }
        }

    }

    fun searchTopics(query: String) {
        searchQuery = query
        if (query.isEmpty()) {
            topics = allTopics
        } else {
            topics = allTopics.filter { topic ->
                topic.title.contains(query, ignoreCase = true) ||
                        topic.subtitle.contains(query, ignoreCase = true) ||
                        topic.sections.any { section ->
                            section.title.contains(query, ignoreCase = true) ||
                                    section.content.contains(query, ignoreCase = true)
                        }
            }
        }
    }

    fun clearSearch() {
        searchTopics("")
    }
}