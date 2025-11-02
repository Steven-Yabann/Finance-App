package com.example.finance_project.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance_project.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf


// VIEW MODEL
class MarketViewModel: ViewModel() {
    private val _stocks = mutableStateListOf<StockData>()
    val stocks: List<StockData> = _stocks

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading


    fun fetchStockPrice(symbol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getGlobalQuote(
                    symbol = symbol,
                    apiKey = "4JT3YHWR9XGLYTRT"
                )
                val quote = response.quote
                val price = quote?.price ?: "N/A"
                val symbolName = quote?.symbol ?: symbol

                // If same stock, remove
                _stocks.removeAll{ it.symbol == symbolName }
                _stocks.add(StockData(symbolName, price))
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching stock price", e)
            } finally {
                _isLoading.value = false
            }
        }

    }
}

data class StockData(
    val symbol: String,
    val price: String
)