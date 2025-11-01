package com.example.finance_project.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance_project.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


// VIEW MODEL
class MarketViewModel: ViewModel() {
    private val _stockPrice = mutableStateOf("Loading...")
    val stockPrice: State<String> = _stockPrice

    init {
        fetchStockPrice("AAPL")
    }

    fun fetchStockPrice(symbol: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGlobalQuote(symbol = symbol, apiKey = "4JT3YHWR9XGLYTRT")
                val price = response.quote?.price
                if (price != null){
                    _stockPrice.value = "$symbol: $$price"
                } else {
                    _stockPrice.value = "Error: No price data available"
                }
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching stock price", e)
                _stockPrice.value = "Error: ${e.message}"
            }
        }
    }


}