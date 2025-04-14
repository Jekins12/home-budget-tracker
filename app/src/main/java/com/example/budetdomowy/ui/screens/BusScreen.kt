package com.example.budetdomowy.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budetdomowy.data.model.BusRide
import com.example.budetdomowy.data.model.DailyExpense
import com.example.budetdomowy.data.storage.StorageManager
import com.example.budetdomowy.ui.components.MonthYearPickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 👇 custom data class to replace Quadruple
data class BusRow(
    val fullDate: String,
    val dayOnly: String,
    val number: String,
    val time: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BusScreen() {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }
    var allDays by remember { mutableStateOf(storage.loadExpenses()) }

    val allMonths = remember {
        allDays.map { it.date.takeLast(7) }.distinct().sortedDescending()
    }

    val todayMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM.yyyy"))
    var selectedMonth by remember {
        mutableStateOf(allMonths.find { it == todayMonth } ?: allMonths.firstOrNull().orEmpty())
    }
    var expanded by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedRide by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    var showMonthPicker by remember { mutableStateOf(false) }

    // Filter + map to BusRow
    val monthDays = allDays
        .filter { it.date.endsWith(selectedMonth) }
        .flatMap { day ->
            day.buses.map { bus ->
                BusRow(
                    fullDate = day.date,
                    dayOnly = day.date.take(2),
                    number = bus.busNumber,
                    time = bus.arrivalTime
                )
            }
        }
        .sortedBy { it.dayOnly.toIntOrNull() ?: 0 }

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Przejazdy: $selectedMonth", style = MaterialTheme.typography.headlineSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Wybierz miesiąc: ")
                        Text(
                            selectedMonth,
                            modifier = Modifier
                                .clickable { showMonthPicker = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Dodaj przejazd")
                }
            }
        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dzień", modifier = Modifier.weight(1f))
                    Text("Autobus", modifier = Modifier.weight(1f))
                    Text("Godzina", modifier = Modifier.weight(1f))
                }

                HorizontalDivider(Modifier.padding(bottom = 8.dp))

                LazyColumn {
                    items(monthDays) { row ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedRide = Triple(row.fullDate, row.number, row.time)
                                    showEditDialog = true
                                }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(row.dayOnly, modifier = Modifier.weight(1f))
                            Text(row.number, modifier = Modifier.weight(1f))
                            Text(row.time, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    )

    if (showAddDialog) {
        AddBusDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { day, number, time ->
                val updated = storage.loadExpenses().toMutableList()
                val fullDate = "${day.padStart(2, '0')}.$selectedMonth"
                val index = updated.indexOfFirst { it.date == fullDate }

                if (index >= 0) {
                    val updatedBuses = updated[index].buses + BusRide(number, time)
                    updated[index] = updated[index].copy(buses = updatedBuses)
                } else {
                    updated.add(
                        DailyExpense(
                            date = fullDate,
                            dayOfWeek = "",
                            buses = listOf(BusRide(number, time))
                        )
                    )
                }

                storage.saveExpenses(updated)
                allDays = updated
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedRide != null) {
        EditBusDialog(
            ride = selectedRide!!,
            onDismiss = { showEditDialog = false },
            onUpdate = { date, newNumber, newTime, oldTime ->
                val updated = storage.loadExpenses().map { day ->
                    if (day.date == date) {
                        val updatedBuses = day.buses.map {
                            if (it.arrivalTime == oldTime) BusRide(newNumber, newTime) else it
                        }
                        day.copy(buses = updatedBuses)
                    } else day
                }
                storage.saveExpenses(updated)
                allDays = updated
                showEditDialog = false
            },
            onDelete = { date, number, time ->
                val updated = storage.loadExpenses().mapNotNull { day ->
                    if (day.date == date) {
                        val updatedBuses = day.buses.filterNot {
                            it.busNumber == number && it.arrivalTime == time
                        }
                        if (updatedBuses.isEmpty()) null else day.copy(buses = updatedBuses)
                    } else day
                }
                storage.saveExpenses(updated)
                allDays = updated
                showEditDialog = false
            }
        )
    }
    if (showMonthPicker) {
        MonthYearPickerDialog(
            initialMonthYear = selectedMonth,
            onMonthSelected = {
                selectedMonth = it
                showMonthPicker = false
            },
            onDismiss = {
                showMonthPicker = false
            }
        )
    }

}
