package com.example.finance_project.ui.market

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance_project.ui.viewmodel.MarketViewModel
import com.example.finance_project.ui.viewmodel.StockData

@Composable
fun MarketScreen(viewModel: MarketViewModel = viewModel()) {
    var selectedStock by remember { mutableStateOf("") }
    val stockOptions = listOf("AAPL", "TSLA", "AMZN", "GOOG", "MSFT")
    val stocks = viewModel.stocks
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Market Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Stock dropdown
        StockSelector(
            stockOptions = stockOptions,
            selectedStock = selectedStock,
            onStockSelected = { selected ->
                selectedStock = selected
                viewModel.fetchStockPrice(selected)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(stocks) { stock ->
                    StockCard(stock)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockSelector(
    stockOptions: List<String>,
    selectedStock: String,
    onStockSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedStock,
            onValueChange = {},
            label = { Text("Choose a stock") },
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            stockOptions.forEach { stock ->
                DropdownMenuItem(
                    text = { Text(stock) },
                    onClick = {
                        onStockSelected(stock)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StockCard(stock: StockData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stock.symbol, style = MaterialTheme.typography.titleMedium)
            Text("$${stock.price}", style = MaterialTheme.typography.titleMedium)
        }
    }
}
