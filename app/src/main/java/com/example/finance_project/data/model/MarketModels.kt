package com.example.finance_project.data.model
import com.google.gson.annotations.SerializedName

// Tell GSON how to parse the JSON response
data class GlobalQuoteResponse(
    @SerializedName("Global Quote")
    val quote: QuoteData?
)

data class QuoteData(
    @SerializedName("01. symbol")
    val symbol: String?,
    @SerializedName("05. price")
    val price: String?,
    @SerializedName(value="10. change percent")
    val changePercent: String?
)


data class CurrencyExchangeResponse (
    @SerializedName("Realtime Currency Exchange Rate")
    val quote: CurrencyExchangeData?
)

data class CurrencyExchangeData (
    @SerializedName("1. From_Currency Code")
    val fromCurrency: String?,
    @SerializedName("3. To_Currency Code")
    val toCurrency: String?,
    @SerializedName("5. Exchange Rate")
    val exchangeRate: String?
)

data class CommoditiesResponse (
    @SerializedName("interval")
    val interval: String?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("data")
    val quote: List<CommoditiesData>?
)

data class CommoditiesData(
    @SerializedName("date")
    val date: String?,
    @SerializedName("value")
    val value: String?
)