package com.example.finance_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CommoditiesScreen() {
    val commodities = listOf(
        "Gold (XAU/USD)" to "2375.23",
        "Silver (XAG/USD)" to "27.91",
        "Crude Oil (WTI)" to "82.34",
        "Brent Oil" to "84.10"
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(commodities) { (name, price) ->
            CommodityCard(name, price)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CommodityCard(name: String, price: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text("$price", fontWeight = FontWeight.Bold)
        }
    }
}
