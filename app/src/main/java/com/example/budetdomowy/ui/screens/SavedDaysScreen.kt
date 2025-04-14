package com.example.budetdomowy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.budetdomowy.data.model.DailyExpense
import com.example.budetdomowy.data.storage.StorageManager

@Composable
fun SavedDaysScreen(navController: NavController) {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }

    var days by remember { mutableStateOf<List<DailyExpense>>(emptyList()) }

    LaunchedEffect(Unit) {
        days = storage.loadExpenses().sortedByDescending { it.date }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zapisane dni", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(days) { day ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            navController.navigate("edit_day/${day.date}")
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${day.date} (${day.dayOfWeek})", style = MaterialTheme.typography.titleMedium)
                        Text("Mieszkanie 1: ${day.apt1.rent + day.apt1.electricity + day.apt1.gas + day.apt1.other} zł")
                        Text("Mieszkanie 2: ${day.apt2.rent + day.apt2.electricity + day.apt2.gas + day.apt2.other} zł")
                        Text("Paliwo: ${day.car.fuel} zł")
                    }
                }
            }
        }
    }
}
