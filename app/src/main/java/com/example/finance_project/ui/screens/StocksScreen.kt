package com.example.finance_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance_project.ui.viewmodel.MarketViewModel
import com.example.finance_project.ui.viewmodel.StockData


@Composable
fun StocksScreen(viewModel: MarketViewModel = viewModel()) {
    val stocks = viewModel.stocks
    val isLoading = viewModel.isLoading.value
    var stockSymbol by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your watchlist", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stockSymbol,
                onValueChange = { stockSymbol = it.uppercase() },
                label = { Text("Enter stock symbol (e.g. AAPL")},
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button (
                onClick = {
                    if (stockSymbol.isNotEmpty() && stocks.size < 5) {
                        viewModel.fetchStockPrice(stockSymbol)
                        stockSymbol = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else if (stocks.isEmpty()) {
            Text("No stocks in your watchlist. Add some from your picker", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn {
                items(stocks) { stock ->
                    StockCard(stock, viewModel)
                    Spacer(Modifier.height(8.dp))
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


