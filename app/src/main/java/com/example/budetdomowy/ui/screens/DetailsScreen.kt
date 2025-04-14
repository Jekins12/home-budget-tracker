package com.example.budetdomowy.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budetdomowy.data.storage.StorageManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(category: String, month: String?) {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }
    val allDays = remember { storage.loadExpenses() }

    val monthToShow = month ?: LocalDate.now().format(DateTimeFormatter.ofPattern("MM.yyyy"))
    val days = allDays
        .filter { it.date.endsWith(monthToShow) }
        .sortedBy { it.date.take(2).toIntOrNull() ?: 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Szczegóły: ${categoryLabel(category)} ($monthToShow)",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        // Header
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Data", modifier = Modifier.weight(1f))
            when (category) {
                "samochod" -> {
                    Text("Paliwo", modifier = Modifier.weight(1f))
                    Text("Przebieg", modifier = Modifier.weight(1f))
                }
                "m1", "m2" -> {
                    Text("Prąd", modifier = Modifier.weight(1f))
                    Text("Czynsz", modifier = Modifier.weight(1f))
                    Text("Gaz", modifier = Modifier.weight(1f))
                    Text("Inne", modifier = Modifier.weight(1f))
                }
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        // Rows
        LazyColumn {
            val filteredDays = when (category) {
                "samochod" -> days.filter { it.car.fuel > 0 || it.car.mileage > 0 }
                "m1" -> days.filter {
                    it.apt1.electricity > 0 || it.apt1.rent > 0 || it.apt1.gas > 0 || it.apt1.other > 0
                }
                "m2" -> days.filter {
                    it.apt2.electricity > 0 || it.apt2.rent > 0 || it.apt2.gas > 0 || it.apt2.other > 0
                }
                else -> emptyList()
            }

            items(filteredDays) { day ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val dayOnly = day.date.take(2) // extracts "14" from "14.04.2025"
                    Text(dayOnly, modifier = Modifier.weight(1f))
                    when (category) {
                        "samochod" -> {
                            Text("%.2f".format(day.car.fuel), modifier = Modifier.weight(1f))
                            Text("${day.car.mileage}", modifier = Modifier.weight(1f))
                        }
                        "m1" -> {
                            Text("%.2f".format(day.apt1.electricity), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt1.rent), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt1.gas), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt1.other), modifier = Modifier.weight(1f))
                        }
                        "m2" -> {
                            Text("%.2f".format(day.apt2.electricity), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt2.rent), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt2.gas), modifier = Modifier.weight(1f))
                            Text("%.2f".format(day.apt2.other), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

fun categoryLabel(code: String): String = when (code) {
    "samochod" -> "Samochód"
    "m1" -> "Mieszkanie 1"
    "m2" -> "Mieszkanie 2"
    else -> code
}
