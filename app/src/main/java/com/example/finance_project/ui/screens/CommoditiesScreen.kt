package com.example.finance_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import com.example.finance_project.ui.viewmodel.CommodityDataPoint
import com.example.finance_project.ui.viewmodel.MarketViewModel
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import androidx.compose.ui.graphics.Brush
import com.example.finance_project.ui.theme.*
import com.example.finance_project.ui.theme.TextSecondary
import com.example.finance_project.ui.theme.TextPrimary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
@Composable
fun CommoditiesScreen(viewModel: MarketViewModel = viewModel()) {
    val commodities = listOf("WHEAT", "COPPER", "ALUMINUM")
    val intervals = listOf("monthly")

    var selectedCommodity by remember { mutableStateOf(commodities.first()) }
    var selectedInterval by remember { mutableStateOf(intervals.first()) }

    val data = viewModel.commodities
    val isLoading by viewModel.isLoading

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DropdownSelector("Commodity", commodities, selectedCommodity) { selectedCommodity = it }
            }
            Box(modifier = Modifier.weight(1f)) {
                DropdownSelector("Interval", intervals, selectedInterval) { selectedInterval = it }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.fetchCommodity(selectedCommodity, selectedInterval) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Data")
        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else if (data.isNotEmpty()) {
            CommodityLineChart(data, selectedCommodity)
        } else {
            Text("No data available", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun CommodityLineChart(
    data: List<CommodityDataPoint>,
    commodity: String
) {
    if (data.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No data to display", color = TextSecondary)
        }
        return
    }

    val commodityColor = getCommodityColor(commodity)

    // Calculate trend
    val firstPrice = data.first().value
    val lastPrice = data.last().value
    val isPositiveTrend = lastPrice >= firstPrice

    // Date formatting
    val inFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outFmt = DateTimeFormatter.ofPattern("MMM yy")

    val points: List<Point> = data.mapIndexed { index, d ->
        Point(index.toFloat(), d.value)
    }

    // Compute min/max for Y axis with padding
    val maxPrice = data.maxOf { it.value }
    val minPrice = data.minOf { it.value }
    val priceRange = maxPrice - minPrice
    val paddedMax = maxPrice + (priceRange * 0.1f)
    val paddedMin = (minPrice - (priceRange * 0.1f)).coerceAtLeast(0f)
    val steps = 5

    // X axis config
    val xAxisData = AxisData.Builder()
        .labelData { idx ->
            val i = idx.coerceIn(0, data.lastIndex)
            try {
                LocalDate.parse(data[i].date, inFmt).format(outFmt)
            } catch (_: Exception) {
                data[i].date.takeLast(5)
            }
        }
        .axisLabelColor(TextSecondary)
        .axisLineColor(Color(0xFFE5E7EB))
        .startDrawPadding(12.dp)
        .build()

    // Y axis config
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelData { stepIndex ->
            val range = paddedMax - paddedMin
            val value = paddedMin + (stepIndex * (range / steps.toFloat()))
            if (value % 1.0f == 0f) {
                value.roundToInt().toString()
            } else {
                "%.2f".format(value)
            }
        }
        .axisLabelColor(TextSecondary)
        .axisLineColor(Color(0xFFE5E7EB))
        .build()

    // Enhanced line style with gradient
    val line = Line(
        dataPoints = points,
        lineStyle = LineStyle(
            color = commodityColor,
            lineType = LineType.SmoothCurve(isDotted = false),
            width = 4f
        ),
        intersectionPoint = IntersectionPoint(
            color = commodityColor,
            radius = 6.dp
        ),
        selectionHighlightPoint = SelectionHighlightPoint(
            color = commodityColor,
            radius = 8.dp
        ),
        shadowUnderLine = ShadowUnderLine(
            brush = Brush.verticalGradient(
                colors = listOf(
                    commodityColor.copy(alpha = 0.3f),
                    commodityColor.copy(alpha = 0.05f),
                    Color.Transparent
                )
            ),
            alpha = 0.5f
        ),
        selectionHighlightPopUp = SelectionHighlightPopUp()
    )

    val linePlot = LinePlotData(lines = listOf(line))

    val lineChartData = LineChartData(
        linePlotData = linePlot,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            color = Color(0xFFE5E7EB).copy(alpha = 0.5f)
        ),
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chart Title with Trend Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$commodity Price Trend",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Icon(
                imageVector = if (isPositiveTrend) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                contentDescription = null,
                tint = if (isPositiveTrend) PositiveGreen else NegativeRed,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Date Range
        Text(
            text = "${data.first().date} → ${data.last().date}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Chart
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            lineChartData = lineChartData
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Y-axis Label
        Text(
            text = "Price (USD / metric ton)",
            style = MaterialTheme.typography.labelMedium.copy(
                color = TextSecondary
            )
        )
    }
}

private fun getCommodityColor(commodity: String): Color {
    return when (commodity.uppercase()) {
        "WHEAT" -> WheatColor
        "COPPER" -> CopperColor
        "ALUMINUM" -> AluminumColor
        "GOLD" -> GoldColor
        else -> NeutralBlue
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
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
