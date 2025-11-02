package com.example.finance_project.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create a single, reusable object for network calls
object RetrofitInstance {
    private const val BASE_URL = "https://www.alphavantage.co/"

    // lazy ensures the object is created once
    val api: FinanceApiService by lazy {
        Retrofit.Builder() // -> creates HTTP client
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApiService::class.java)
    }
}

