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
    private val _topics = MutableStateFlow<List<TopicModel>>(emptyList())
    val topics: StateFlow<List<TopicModel>> = _topics


    fun loadTopics(context: Context) {
        viewModelScope.launch {
            val repository = TopicRepository(context)
            val data = repository.loadTopics()
            Log.d("LearnViewModel", "Loaded ${data.size} topics")
            _topics.value = data
        }
    }

}
