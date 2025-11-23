package com.example.finance_project.data.remote

import com.example.finance_project.data.model.GlobalQuoteResponse
import com.example.finance_project.data.model.CurrencyExchangeResponse
import com.example.finance_project.data.model.CommoditiesResponse
import com.example.finance_project.data.model.CryptoNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Define the endpoints, query parameters, and expected responses.
interface FinanceApiService {
    // Get the global quote for a stock
    // suspend function means safe to run in a coroutine
    @GET("query")
    suspend fun getGlobalQuote(
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): GlobalQuoteResponse

    @GET("query")
    suspend fun getCurrencyExchange(
        @Query("function") function: String = "CURRENCY_EXCHANGE_RATE",
        @Query("from_currency") fromCurrency: String,
        @Query("to_currency") toCurrency: String,
        @Query("apikey") apiKey: String
    ) : CurrencyExchangeResponse

    @GET("query")
    suspend fun getCommodities(
        @Query("function") function: String,
        @Query("interval") interval: String,
        @Query("apikey") apiKey: String
    ) : CommoditiesResponse
}

// Separate interface for crypto news API
interface CryptoNewsApiService {
    @GET("v2/news/")
    suspend fun getCryptoNews(
        @Query("lang") language: String = "EN",
        @Query("sortOrder") sortOrder: String = "latest",
        @Query("api_key") apiKey: String
    ): CryptoNewsResponse
}
