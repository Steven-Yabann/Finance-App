package com.example.finance_project.data.remote

import com.example.finance_project.data.model.GlobalQuoteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceApiService {
    // Get the global quote for a stock
    @GET("query")
    suspend fun getGlobalQuote(
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): GlobalQuoteResponse
}
