package com.example.finance_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import com.example.finance_project.ui.theme.Finance_ProjectTheme

// --- DATA MODELS ---

data class Commodity(val name: String, val symbol: String, val price: String, val change: String, val percent: String)
data class Currency(val pair: String, val symbol: String, val price: String, val change: String, val percent: String)
data class Stock(val name: String, val symbol: String, val price: String, val change: String, val percent: String)

// --- MAIN MARKET SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Commodities", "Currencies", "Stocks")

    Scaffold(
        topBar = { MarketTopBar() },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // --- Title and Subtitle ---
            Text(
                text = "Market Trends",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Real-time market data to understand investment opportunities",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Buttons ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton("Live Data", Icons.Outlined.Bolt)
                FilterButton("Refresh", Icons.Default.Refresh)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Tabs ---
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF9F5FF),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.Gray,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Tab Content ---
            when (selectedTab) {
                0 -> CommoditiesTab()
                1 -> CurrenciesTab()
                2 -> StocksTab()
            }
        }
    }
}

// --- TOP BAR ---
@Composable
fun MarketTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(onClick = { /* Open menu */ }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Menu",
                tint = Color.Black
            )
        }
    }
}

// --- FILTER BUTTON ---
@Composable
fun FilterButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedButton(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(8.dp),
        border = ButtonDefaults.outlinedButtonBorder,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 14.sp)
    }
}

// --- COMMODITIES TAB ---
@Composable
fun CommoditiesTab() {
    val commodities = listOf(
        Commodity("Gold", "XAU/USD", "2,042.5/oz", "+15.25", "+0.75%"),
        Commodity("Silver", "XAG/USD", "24.85/oz", "-0.35", "-1.39%")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(commodities.size) { index ->
            MarketCard(
                title = commodities[index].name,
                subtitle = commodities[index].symbol,
                value = commodities[index].price,
                change = commodities[index].change,
                percent = commodities[index].percent
            )
        }
    }
}

// --- CURRENCIES TAB ---
@Composable
fun CurrenciesTab() {
    val currencies = listOf(
        Currency("EUR/USD", "EURUSD", "1.0856", "+0.0023", "+0.21%"),
        Currency("GBP/USD", "GBPUSD", "1.2534", "-0.0067", "-0.53%"),
        Currency("USD/JPY", "USDJPY", "149.8500", "+0.4500", "+0.30%"),
        Currency("USD/CHF", "USDCHF", "0.8967", "+0.0012", "+0.13%")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(currencies.size) { index ->
            MarketCard(
                title = currencies[index].pair,
                subtitle = currencies[index].symbol,
                value = currencies[index].price,
                change = currencies[index].change,
                percent = currencies[index].percent
            )
        }
    }
}

// --- STOCKS TAB ---
@Composable
fun StocksTab() {
    val stocks = listOf(
        Stock("Apple Inc.", "AAPL", "225.50", "+1.25", "+0.56%"),
        Stock("Tesla Inc.", "TSLA", "255.40", "-2.75", "-1.05%"),
        Stock("Amazon", "AMZN", "175.80", "+3.45", "+2.00%")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(stocks.size) { index ->
            MarketCard(
                title = stocks[index].name,
                subtitle = stocks[index].symbol,
                value = stocks[index].price,
                change = stocks[index].change,
                percent = stocks[index].percent
            )
        }
    }
}

// --- REUSABLE CARD ---
@Composable
fun MarketCard(title: String, subtitle: String, value: String, change: String, percent: String) {
    val isPositive = percent.contains("+")
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(subtitle, color = Color.Gray, fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier
                        .background(
                            if (isPositive) Color(0xFF1A1A2E) else Color(0xFFF8D7DA),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = percent,
                        color = if (isPositive) Color.White else Color(0xFFB00020),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Start
            )
            Text(
                change,
                color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFB00020),
                fontSize = 13.sp
            )
        }
    }
}




// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    Finance_ProjectTheme {
        MarketScreen()
    }
}
