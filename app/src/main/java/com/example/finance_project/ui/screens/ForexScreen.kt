package com.example.finance_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finance_project.ui.viewmodel.MarketViewModel

@Composable
fun ForexScreen(viewModel: MarketViewModel) {
    val forexList = viewModel.forex
    val isLoading by viewModel.isLoading
    var fromCurrency by remember { mutableStateOf("") }
    var toCurrency by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Live Forex Rates", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = fromCurrency,
                onValueChange = { fromCurrency = it.uppercase() },
                label = { Text("From (e.g. USD)") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = toCurrency,
                onValueChange = { toCurrency = it.uppercase() },
                label = { Text("To (e.g. EUR)") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (fromCurrency.isNotEmpty() && toCurrency.isNotEmpty() && forexList.size < 5) {
                    viewModel.fetchCurrencyExchange(fromCurrency, toCurrency)
                    fromCurrency = ""
                    toCurrency = ""
                }
            },
            enabled = fromCurrency.isNotEmpty() && toCurrency.isNotEmpty() && forexList.size < 5
        ) {
            Text("Add Pair")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (forexList.isEmpty()) {
            Text("No forex pairs yet. Add some to view live exchange rates.")
        } else {
            LazyColumn {
                items(forexList.size) { index ->
                    val forex = forexList[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${forex.fromCurrency}/${forex.toCurrency}", style = MaterialTheme.typography.titleMedium)
                                Text("Rate: ${forex.exchangeRate}")
                            }

                            TextButton(onClick = { viewModel.removeForex(forex.fromCurrency, forex.toCurrency) }) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }
    }
}
