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
import com.example.finance_project.data.model.CommoditiesData
import java.time.LocalDate
import java.time.format.DateTimeFormatter



// VIEW MODEL
// Fetch data from the API and expose it to the UI reactively.
class MarketViewModel: ViewModel() {
    private val _stocks = mutableStateListOf<StockData>()
    val stocks: List<StockData> = _stocks
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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

    private val _commodities = mutableStateListOf<CommodityDataPoint>()
    val commodities: List<CommodityDataPoint> = _commodities

    fun fetchCommodity(commodity: String, interval: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getCommodities(
                    function = commodity,
                    interval = interval,
                    apiKey = "4JT3YHWR9XGLYTRT"
                )

                val actualInterval = response.interval ?: interval
                Log.d("MarketViewModel", "API returned interval: $actualInterval")

                val rawData = response.quote ?: emptyList()
                if (rawData.isEmpty()) {
                    Log.w("MarketViewModel", "No data for $commodity ($actualInterval)")
                    _commodities.clear()
                    return@launch
                }

                val filtered = filterDataByInterval(rawData, actualInterval)

                _commodities.clear()
                _commodities.addAll(filtered.map {
                    CommodityDataPoint(
                        date = it.date ?: "",
                        value = it.value?.toFloatOrNull() ?: 0f
                    )
                })
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching commodity data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun filterDataByInterval(data: List<CommoditiesData>, interval: String): List<CommoditiesData> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val cutoff = when (interval.lowercase()) {
            "daily" -> today.minusDays(7)
            "weekly" -> today.minusWeeks(10)
            "monthly" -> today.minusYears(1)
            else -> today.minusYears(2)
        }

        return data.filter {
            try {
                val date = LocalDate.parse(it.date, formatter)
                date.isAfter(cutoff)
            } catch (e: Exception) {
                false
            }
        }.sortedBy { it.date }
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

data class CommodityDataPoint(
    val date: String,
    val value: Float
)