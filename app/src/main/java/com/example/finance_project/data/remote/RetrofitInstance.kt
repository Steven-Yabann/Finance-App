package com.example.finance_project.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://www.alphavantage.co/"

    // lazy ensures the object is created oncce
    val api: FinanceApiService by lazy {
        Retrofit.Builder() // -> creates HTTP client
            // the url where we will fetch data
            .baseUrl(BASE_URL)
            // Gson converts Json Data to Kotlin Objects
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApiService::class.java)
    }
}

