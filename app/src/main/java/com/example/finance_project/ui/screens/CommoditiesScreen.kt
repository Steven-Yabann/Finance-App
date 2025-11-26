package com.example.finance_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.finance_project.ui.viewmodel.CommodityDataPoint
import com.example.finance_project.ui.viewmodel.MarketViewModel

@Composable
fun CommoditiesScreen(viewModel: MarketViewModel = viewModel()) {
    // Available options
    val commodities = listOf("WHEAT", "COPPER", "ALUMINIUM")
    val intervals = listOf("monthly")

    // Currently selected options
    var selectedCommodity by remember { mutableStateOf("WHEAT") }
    var selectedInterval by remember { mutableStateOf("monthly") }

    // Data from ViewModel
    val data = viewModel.commodities
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dropdown selectors
        SimpleDropdown(
            label = "Commodity",
            options = commodities,
            selected = selectedCommodity,
            onSelect = { selectedCommodity = it }
        )

        Spacer(Modifier.height(8.dp))

        SimpleDropdown(
            label = "Interval",
            options = intervals,
            selected = selectedInterval,
            onSelect = { selectedInterval = it }
        )

        Spacer(Modifier.height(16.dp))

        // Load button
        Button(
            onClick = {
                viewModel.fetchCommodity(selectedCommodity, selectedInterval)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Data")
        }

        Spacer(Modifier.height(24.dp))

        // Display area
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            data.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available. Click 'Load Data' to begin.",
                        color = Color.Gray
                    )
                }
            }
            else -> {
                SimpleCommodityChart(data, selectedCommodity)
            }
        }
    }
}

@Composable
fun SimpleCommodityChart(
    data: List<CommodityDataPoint>,
    commodity: String
) {
    // Convert data to points for the chart
    val points = data.mapIndexed { index, dataPoint ->
        Point(x = index.toFloat(), y = dataPoint.value)
    }

    // Simple color selection
    val chartColor = when (commodity) {
        "WHEAT" -> Color(0xFFE8B44C)
        "COPPER" -> Color(0xFFD9824E)
        "ALUMINUM" -> Color(0xFF8FA1B3)
        else -> Color(0xFF6366F1)
    }

    // Configure X axis (dates)
    val xAxisData = AxisData.Builder()
        .labelData { index ->
            if (index in data.indices) {
                data[index].date.takeLast(5) // Show last 5 chars of date
            } else ""
        }
        .axisLineColor(Color.LightGray)
        .build()

    // Configure Y axis (prices)
    val maxPrice = data.maxOf { it.value }
    val minPrice = data.minOf { it.value }

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelData { step ->
            val value = minPrice + (step * (maxPrice - minPrice) / 5)
            "%.0f".format(value)
        }
        .axisLineColor(Color.LightGray)
        .build()

    // Create the line
    val line = Line(
        dataPoints = points,
        lineStyle = LineStyle(
            color = chartColor,
            lineType = LineType.SmoothCurve(isDotted = false)
        ),
        intersectionPoint = IntersectionPoint(color = chartColor),
        selectionHighlightPoint = SelectionHighlightPoint(color = chartColor)
    )

    // Combine everything
    val chartData = LineChartData(
        linePlotData = LinePlotData(lines = listOf(line)),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.LightGray.copy(alpha = 0.5f)),
        backgroundColor = Color.White
    )

    Column {
        // Title
        Text(
            text = "$commodity Price Chart",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Chart
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = chartData
        )

        // Label
        Text(
            text = "Price (USD per metric ton)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}