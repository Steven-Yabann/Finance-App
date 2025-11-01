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
    val price: String?
)

