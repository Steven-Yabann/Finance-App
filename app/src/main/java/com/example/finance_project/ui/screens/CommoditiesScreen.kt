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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
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
            CommodityLineChart(data)
        } else {
            Text("No data available", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun CommodityLineChart(data: List<CommodityDataPoint>) {
    if (data.isEmpty()) {
        // nothing to draw
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No data to display", color = Color.Gray)
        }
        return
    }

    // prepare short X labels: "MMM yy"
    val inFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outFmt = DateTimeFormatter.ofPattern("MMM yy")

    val points: List<Point> = data.mapIndexed { index, d ->
        Point(index.toFloat(), d.value)
    }

    // compute min/max for Y axis
    val maxPrice = data.maxOf { it.value }
    val minPrice = data.minOf { it.value }
    val steps = 5

    // X axis config: show short labels and skip some to avoid crowding
    val xAxisData = AxisData.Builder()
        .labelData { idx ->
            val i = idx.coerceIn(0, data.lastIndex)
            try {
                LocalDate.parse(data[i].date, inFmt).format(outFmt)
            } catch (_: Exception) {
                // fallback: last 5 chars of date
                data[i].date.takeLast(5)
            }
        }
        .axisLabelColor(Color.Gray)
        .axisLineColor(Color.LightGray)
        .startDrawPadding(8.dp)
        .build()

    // Y axis config: numeric labels between min and max
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelData { stepIndex ->
            val range = maxPrice - minPrice
            val value = minPrice + (stepIndex * (range / steps.toFloat()))
            // format with no trailing .0 when not needed
            if (value % 1.0f == 0f) {
                value.roundToInt().toString()
            } else {
                "%.2f".format(value)
            }
        }
        .axisLabelColor(Color.Gray)
        .axisLineColor(Color.LightGray)
        .build()

    // Line style
    val line = Line(
        dataPoints = points,
        lineStyle = LineStyle(color = MaterialTheme.colorScheme.primary, width = 3f),
        // use defaults for point style (no dots) and smoothness handled by library defaults
    )

    val linePlot = LinePlotData(lines = listOf(line))

    val lineChartData = LineChartData(
        linePlotData = linePlot,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.LightGray.copy(alpha = 0.35f)),
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Commodity Price (Monthly)",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = Color.Black
        )

        Text(
            text = "${data.first().date} → ${data.last().date}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            lineChartData = lineChartData
        )

        Text(
            text = "Price (USD / metric ton)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 8.dp)
        )
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
