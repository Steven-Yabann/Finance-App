package com.example.finance_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance_project.ui.viewmodel.MarketViewModel
import com.example.finance_project.ui.viewmodel.StockData


@Composable
fun StocksScreen(viewModel: MarketViewModel = viewModel()) {
    val stocks = viewModel.stocks
    val isLoading = viewModel.isLoading.value
    var stockSymbol by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Popular stock symbols for quick access
    val popularStocks = listOf(
        "AAPL" to "Apple",
        "GOOGL" to "Google",
        "MSFT" to "Microsoft", 
        "AMZN" to "Amazon",
        "TSLA" to "Tesla",
        "NVDA" to "NVIDIA",
        "META" to "Meta",
        "NFLX" to "Netflix"
    )

    // Filter popular stocks based on search
    val filteredPopularStocks = popularStocks.filter { (symbol, name) ->
        if (searchQuery.isEmpty()) true
        else symbol.contains(searchQuery, ignoreCase = true) || 
             name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Stock Watchlist",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Track your favorite stocks • ${stocks.size}/5 stocks",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                stockSymbol = it.uppercase()
            },
            label = { Text("Search stocks") },
            placeholder = { Text("AAPL, Apple, etc.") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { 
                            searchQuery = ""
                            stockSymbol = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (stockSymbol.isNotEmpty() && stocks.size < 5) {
                        viewModel.fetchStockPrice(stockSymbol)
                        searchQuery = ""
                        stockSymbol = ""
                    }
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Add Stock Button
        if (stockSymbol.isNotEmpty()) {
            Button(
                onClick = {
                    if (stocks.size < 5) {
                        viewModel.fetchStockPrice(stockSymbol)
                        searchQuery = ""
                        stockSymbol = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = stocks.size < 5
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add $stockSymbol to Watchlist")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Popular Stocks Section (show when not searching or when search has results)
        if (searchQuery.isEmpty() || filteredPopularStocks.isNotEmpty()) {
            Text(
                text = if (searchQuery.isEmpty()) "Popular Stocks" else "Suggestions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredPopularStocks) { (symbol, name) ->
                    Card(
                        modifier = Modifier
                            .clickable {
                                if (stocks.size < 5) {
                                    viewModel.fetchStockPrice(symbol)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (stocks.any { it.symbol == symbol }) 
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = symbol,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (stocks.any { it.symbol == symbol })
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // No suggestions found
        if (searchQuery.isNotEmpty() && filteredPopularStocks.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "No suggestions",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No suggestions found",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Try typing a stock symbol directly",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Current Watchlist
        if (stocks.isNotEmpty()) {
            Text(
                text = "Your Watchlist",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Loading stock data...")
                }
            }
        } else if (stocks.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Empty watchlist",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No stocks in watchlist",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Search and add stocks to start tracking",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stocks) { stock ->
                    StockCard(stock, viewModel)
                }
            }
        }

        // Watchlist limit message
        if (stocks.size >= 5) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Watchlist full! Remove a stock to add more.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun StockCard(
    stock: StockData,
    viewModel: MarketViewModel = viewModel()
) {
    val changeColor = if (stock.changePercent.contains("-")) {
        MaterialTheme.colorScheme.error
    }else {
        Color(0xFF4CAF50)
    }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (horizontalAlignment = Alignment.End) {
                Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
                Text("Price: ${stock.price}")
                Text("Change: ${stock.changePercent}", color = changeColor)
            }

            TextButton(onClick = { viewModel.removeStock(stock.symbol)}) {
                Text("Remove")
            }
        }
    }
}


