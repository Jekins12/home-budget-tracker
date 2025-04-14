package com.example.budetdomowy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budetdomowy.data.model.*
import com.example.budetdomowy.data.storage.StorageManager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.navigation.NavController

@Composable
fun EditDayScreen(date: String, navController: NavController) {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }

    var entry by remember { mutableStateOf<DailyExpense?>(null) }

    LaunchedEffect(Unit) {
        val loaded = storage.loadExpenses().find { it.date == date }
        entry = loaded
    }

    if (entry == null) {
        Text("Nie znaleziono danych dla daty: $date")
        return
    }

    // Convert to editable states
    val apt1 = remember { entry!!.apt1.copy() }
    val apt2 = remember { entry!!.apt2.copy() }
    val car = remember { entry!!.car.copy() }

    val apt1Electricity = remember { mutableStateOf(apt1.electricity.toString()) }
    val apt1Rent = remember { mutableStateOf(apt1.rent.toString()) }
    val apt1Gas = remember { mutableStateOf(apt1.gas.toString()) }
    val apt1Other = remember { mutableStateOf(apt1.other.toString()) }

    val apt2Electricity = remember { mutableStateOf(apt2.electricity.toString()) }
    val apt2Rent = remember { mutableStateOf(apt2.rent.toString()) }
    val apt2Gas = remember { mutableStateOf(apt2.gas.toString()) }
    val apt2Other = remember { mutableStateOf(apt2.other.toString()) }

    val fuel = remember { mutableStateOf(car.fuel.toString()) }
    val mileage = remember { mutableStateOf(car.mileage.toString()) }

    fun toDoubleSafe(text: String) = text.toDoubleOrNull() ?: 0.0
    fun toIntSafe(text: String) = text.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Edytuj dane dla: ${entry!!.date} (${entry!!.dayOfWeek})", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("Mieszkanie 1", style = MaterialTheme.typography.titleMedium)
        ExpenseInputs(apt1Electricity, apt1Rent, apt1Gas, apt1Other)

        Spacer(Modifier.height(16.dp))
        Text("Mieszkanie 2", style = MaterialTheme.typography.titleMedium)
        ExpenseInputs(apt2Electricity, apt2Rent, apt2Gas, apt2Other)

        Spacer(Modifier.height(16.dp))
        Text("Samochód", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = fuel.value,
            onValueChange = { fuel.value = it },
            label = { Text("Paliwo (zł)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mileage.value,
            onValueChange = { mileage.value = it },
            label = { Text("Przebieg") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    val updated = entry!!.copy(
                        apt1 = ExpenseDetails(
                            toDoubleSafe(apt1Electricity.value),
                            toDoubleSafe(apt1Rent.value),
                            toDoubleSafe(apt1Gas.value),
                            toDoubleSafe(apt1Other.value)
                        ),
                        apt2 = ExpenseDetails(
                            toDoubleSafe(apt2Electricity.value),
                            toDoubleSafe(apt2Rent.value),
                            toDoubleSafe(apt2Gas.value),
                            toDoubleSafe(apt2Other.value)
                        ),
                        car = CarExpense(
                            toDoubleSafe(fuel.value),
                            toIntSafe(mileage.value)
                        )
                    )
                    storage.addOrUpdateExpense(updated)
                    Toast.makeText(context, "Zaktualizowano dane!", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Zapisz zmiany")
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                onClick = {
                    storage.deleteExpenseByDate(entry!!.date)
                    Toast.makeText(context, "Usunięto dzień ${entry!!.date}", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // 👈 go back to saved days screen
                }
            ) {
                Text("Usuń dzień")
            }
        }
    }
}
