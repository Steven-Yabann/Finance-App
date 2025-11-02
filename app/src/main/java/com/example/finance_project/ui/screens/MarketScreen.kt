package com.example.finance_project.ui.market

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance_project.ui.screens.CommoditiesScreen
import com.example.finance_project.ui.screens.ForexScreen
import com.example.finance_project.ui.screens.StocksScreen
import com.example.finance_project.ui.viewmodel.MarketViewModel
import com.example.finance_project.ui.viewmodel.StockData

@Composable
fun MarketScreen(viewModel: MarketViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Stocks", "Commodites", "Forex")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index},
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> StocksScreen(viewModel)
            1 -> CommoditiesScreen()
            2 -> ForexScreen(viewModel)
        }
    }
}


