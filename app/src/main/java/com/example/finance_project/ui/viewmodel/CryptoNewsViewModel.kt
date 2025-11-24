package com.example.finance_project.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance_project.data.model.CryptoNewsArticle
import com.example.finance_project.data.repository.CryptoNewsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CryptoNewsViewModel : ViewModel() {
    
    private val repository = CryptoNewsRepository()
    
    private val _newsState = mutableStateOf(CryptoNewsState())
    val newsState: State<CryptoNewsState> = _newsState
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    
    private val _allNews = mutableStateOf<List<CryptoNewsArticle>>(emptyList())
    
    init {
        loadCryptoNews()
    }
    
    fun loadCryptoNews() {
        viewModelScope.launch {
            _newsState.value = _newsState.value.copy(isLoading = true, error = null)
            
            repository.getCryptoNews()
                .onSuccess { response ->
                    _allNews.value = response.data
                    _newsState.value = _newsState.value.copy(
                        isLoading = false,
                        news = response.data,
                        error = null
                    )
                    // Apply current search filter if any
                    if (_searchQuery.value.isNotEmpty()) {
                        searchNews(_searchQuery.value)
                    }
                }
                .onFailure { exception ->
                    _newsState.value = _newsState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    fun searchNews(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _newsState.value = _newsState.value.copy(news = _allNews.value)
        } else {
            val filteredNews = _allNews.value.filter { article ->
                article.title.contains(query, ignoreCase = true) ||
                article.body.contains(query, ignoreCase = true) ||
                article.tags.contains(query, ignoreCase = true) ||
                article.categories.contains(query, ignoreCase = true)
            }
            _newsState.value = _newsState.value.copy(news = filteredNews)
        }
    }
    
    fun clearSearch() {
        searchNews("")
    }
    
    fun retryLoading() {
        loadCryptoNews()
    }
}

data class CryptoNewsState(
    val isLoading: Boolean = false,
    val news: List<CryptoNewsArticle> = emptyList(),
    val error: String? = null
)

// Extension function to format timestamp to readable time
fun CryptoNewsArticle.getFormattedTime(): String {
    val date = Date(publishedOn * 1000) // Convert seconds to milliseconds
    val now = Date()
    val diffInMillis = now.time - date.time
    
    return when {
        diffInMillis < 60_000 -> "Just now"
        diffInMillis < 3600_000 -> "${diffInMillis / 60_000} minutes ago"
        diffInMillis < 86400_000 -> "${diffInMillis / 3600_000} hours ago"
        else -> "${diffInMillis / 86400_000} days ago"
    }
}

// Extension function to get category from tags
fun CryptoNewsArticle.getMainCategory(): String {
    return when {
        categories.contains("BTC", ignoreCase = true) || tags.contains("Bitcoin", ignoreCase = true) -> "Bitcoin"
        categories.contains("ETH", ignoreCase = true) || tags.contains("Ethereum", ignoreCase = true) -> "Ethereum"
        categories.contains("DeFi", ignoreCase = true) || tags.contains("DeFi", ignoreCase = true) -> "DeFi"
        categories.contains("NFT", ignoreCase = true) || tags.contains("NFT", ignoreCase = true) -> "NFT"
        categories.contains("Regulation", ignoreCase = true) || tags.contains("Regulation", ignoreCase = true) -> "Regulation"
        else -> "General"
    }
}

// Extension function to determine sentiment (basic keyword analysis)
fun CryptoNewsArticle.getSentiment(): Pair<String, androidx.compose.ui.graphics.Color> {
    val bullishKeywords = listOf("bullish", "surge", "rally", "gains", "up", "rise", "positive", "breakthrough")
    val bearishKeywords = listOf("bearish", "crash", "drop", "fall", "decline", "negative", "loss", "concern")
    
    val content = "$title $body".lowercase()
    val bullishCount = bullishKeywords.count { content.contains(it) }
    val bearishCount = bearishKeywords.count { content.contains(it) }
    
    return when {
        bullishCount > bearishCount -> "Bullish" to androidx.compose.ui.graphics.Color(0xFF4CAF50)
        bearishCount > bullishCount -> "Bearish" to androidx.compose.ui.graphics.Color(0xFFF44336)
        else -> "Neutral" to androidx.compose.ui.graphics.Color(0xFF757575)
    }
}