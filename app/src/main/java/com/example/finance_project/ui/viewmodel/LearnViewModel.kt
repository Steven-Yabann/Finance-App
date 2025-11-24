package com.example.finance_project.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance_project.data.model.TopicModel
import com.example.finance_project.data.repository.TopicRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LearnViewModel : ViewModel() {
    private val _allTopics = MutableStateFlow<List<TopicModel>>(emptyList())
    private val _topics = MutableStateFlow<List<TopicModel>>(emptyList())
    val topics: StateFlow<List<TopicModel>> = _topics
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun loadTopics(context: Context) {
        viewModelScope.launch {
            val repository = TopicRepository(context)
            val data = repository.loadTopics()
            Log.d("LearnViewModel", "Loaded ${data.size} topics")
            _allTopics.value = data
            _topics.value = data
            // Apply current search if any
            if (_searchQuery.value.isNotEmpty()) {
                searchTopics(_searchQuery.value)
            }
        }
    }
    
    fun searchTopics(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _topics.value = _allTopics.value
        } else {
            val filteredTopics = _allTopics.value.filter { topic ->
                topic.title.contains(query, ignoreCase = true) ||
                topic.subtitle.contains(query, ignoreCase = true) ||
                topic.sections.any { section -> 
                    section.title.contains(query, ignoreCase = true) ||
                    section.content.contains(query, ignoreCase = true)
                }
            }
            _topics.value = filteredTopics
        }
    }
    
    fun clearSearch() {
        searchTopics("")
    }
}
