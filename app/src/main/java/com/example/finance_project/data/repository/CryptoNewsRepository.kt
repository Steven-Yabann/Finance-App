package com.example.finance_project.data.repository

import com.example.finance_project.data.model.CryptoNewsResponse
import com.example.finance_project.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoNewsRepository {
    
    suspend fun getCryptoNews(): Result<CryptoNewsResponse> = withContext(Dispatchers.IO) {
        try {
            // CryptoCompare offers free tier without API key for limited requests
            // For production, you should get a free API key from cryptocompare.com
            val response = RetrofitInstance.cryptoNewsApi.getCryptoNews(
                language = "EN",
                sortOrder = "latest",
                apiKey = "" // Free tier doesn't require API key for basic usage
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}