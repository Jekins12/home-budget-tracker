package com.example.budetdomowy.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budetdomowy.data.storage.StorageManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryScreen(navController: NavController) {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }
    val allDays = remember { storage.loadExpenses() }

    // 🔍 Find available months from data
    val allMonths = remember {
        allDays.mapNotNull { it.date.takeLast(7) }
            .distinct()
            .sortedDescending()
    }

    // 🎯 Selected month (default: current month if available, else first found)
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM.yyyy"))
    var selectedMonth by remember { mutableStateOf(allMonths.find { it == today } ?: allMonths.firstOrNull().orEmpty()) }
    var expanded by remember { mutableStateOf(false) }

    val monthDays = allDays.filter { it.date.endsWith(selectedMonth) }

    // Totals per category
    val m1Electricity = monthDays.sumOf { it.apt1.electricity }
    val m1Rent = monthDays.sumOf { it.apt1.rent }
    val m1Gas = monthDays.sumOf { it.apt1.gas }
    val m1Other = monthDays.sumOf { it.apt1.other }

    val m2Electricity = monthDays.sumOf { it.apt2.electricity }
    val m2Rent = monthDays.sumOf { it.apt2.rent }
    val m2Gas = monthDays.sumOf { it.apt2.gas }
    val m2Other = monthDays.sumOf { it.apt2.other }

    val fuel = monthDays.sumOf { it.car.fuel }

    val totalM1 = m1Electricity + m1Rent + m1Gas + m1Other
    val totalM2 = m2Electricity + m2Rent + m2Gas + m2Other
    val grandTotal = totalM1 + totalM2 + fuel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔽 MONTH SELECTOR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Wybierz miesiąc: ", style = MaterialTheme.typography.titleMedium)

            Box {
                Text(
                    selectedMonth,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allMonths.forEach { month ->
                        DropdownMenuItem(
                            text = { Text(month) },
                            onClick = {
                                selectedMonth = month
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        SummaryCard("Mieszkanie 1", listOf(
            "Prąd" to m1Electricity,
            "Czynsz" to m1Rent,
            "Gaz" to m1Gas,
            "Inne" to m1Other,
            "Suma" to totalM1
        )) {
            navController.navigate("details/m1?month=$selectedMonth")
        }

        Spacer(Modifier.height(16.dp))

        SummaryCard("Mieszkanie 2", listOf(
            "Prąd" to m2Electricity,
            "Czynsz" to m2Rent,
            "Gaz" to m2Gas,
            "Inne" to m2Other,
            "Suma" to totalM2
        )) {
            navController.navigate("details/m2?month=$selectedMonth")
        }

        Spacer(Modifier.height(16.dp))

        SummaryCard("Samochód", listOf(
            "Paliwo" to fuel
        )) {
            navController.navigate("details/samochod?month=$selectedMonth")
        }

        Divider(Modifier.padding(vertical = 16.dp))

        Text("Suma całkowita: %.2f zł".format(grandTotal), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun SummaryCard(
    title: String,
    values: List<Pair<String, Double>>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            values.forEach { (label, value) ->
                Text("$label: %.2f zł".format(value))
            }
        }
    }
}
