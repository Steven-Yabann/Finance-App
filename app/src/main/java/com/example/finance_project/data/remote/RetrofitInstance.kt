package com.example.finance_project.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create a single, reusable object for network calls
object RetrofitInstance {
    private const val BASE_URL = "https://www.alphavantage.co/"
    private const val CRYPTO_NEWS_BASE_URL = "https://min-api.cryptocompare.com/data/"

    // lazy ensures the object is created once
    val api: FinanceApiService by lazy {
        Retrofit.Builder() // -> creates HTTP client
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApiService::class.java)
    }
    
    val cryptoNewsApi: CryptoNewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(CRYPTO_NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoNewsApiService::class.java)
    }
}

