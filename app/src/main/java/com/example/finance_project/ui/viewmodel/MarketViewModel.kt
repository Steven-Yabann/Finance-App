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
// Fetch data from the API and expose it to the UI reactively.
class MarketViewModel: ViewModel() {
    private val _stocks = mutableStateListOf<StockData>()
    val stocks: List<StockData> = _stocks
    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun removeStock(symbol: String) {
        _stocks.removeAll { it.symbol == symbol }
    }

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
                val changePercent = quote?.changePercent?: "0.00%"


                // If same stock, remove
                _stocks.removeAll{ it.symbol == symbolName }
                _stocks.add(StockData(symbolName, price, changePercent))
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching stock price", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _forex = mutableStateListOf<ForexData>()
    val forex: List<ForexData> = _forex

    fun fetchCurrencyExchange(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getCurrencyExchange(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    apiKey = "4JT3YHWR9XGLYTRT"
                )
                val forex = response.quote
                val exchangeRate = forex?.exchangeRate ?: "N/A"
                val fromCurrencyCode = forex?.fromCurrency ?: fromCurrency
                val toCurrencyCode = forex?.toCurrency ?: toCurrency

                // If same stock, remove

                _forex.add(ForexData(fromCurrencyCode, toCurrencyCode, exchangeRate))

            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching currency exchange", e)
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun removeForex(fromCurrency: String, toCurrency: String) {
        _forex.removeAll { it.fromCurrency == fromCurrency && it.toCurrency == toCurrency }
    }
}

data class ForexData(
    val fromCurrency: String,
    val toCurrency: String,
    val exchangeRate: String
)


data class StockData(
    val symbol: String,
    val price: String,
    val changePercent: String = "0.00%"
)