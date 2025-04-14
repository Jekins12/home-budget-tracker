package com.example.budetdomowy.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budetdomowy.data.model.*
import com.example.budetdomowy.data.storage.StorageManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import com.example.budetdomowy.ui.components.DatePickerDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetScreen() {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }

    // Date
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("pl"))

    val date = remember { mutableStateOf(today.format(dateFormatter)) }
    val day = remember { mutableStateOf(today.format(dayFormatter).replaceFirstChar { it.uppercaseChar() }) }

    // Mieszkanie 1
    val apt1Electricity = remember { mutableStateOf("") }
    val apt1Rent = remember { mutableStateOf("") }
    val apt1Gas = remember { mutableStateOf("") }
    val apt1Other = remember { mutableStateOf("") }

    // Mieszkanie 2
    val apt2Electricity = remember { mutableStateOf("") }
    val apt2Rent = remember { mutableStateOf("") }
    val apt2Gas = remember { mutableStateOf("") }
    val apt2Other = remember { mutableStateOf("") }

    // Auto
    val fuel = remember { mutableStateOf("") }
    val mileage = remember { mutableStateOf("") }

    fun toDoubleSafe(text: String): Double = text.toDoubleOrNull() ?: 0.0
    fun toIntSafe(text: String): Int = text.toIntOrNull() ?: 0

    fun saveData() {
        val entry = DailyExpense(
            date = date.value,
            dayOfWeek = day.value,
            apt1 = ExpenseDetails(
                electricity = toDoubleSafe(apt1Electricity.value),
                rent = toDoubleSafe(apt1Rent.value),
                gas = toDoubleSafe(apt1Gas.value),
                other = toDoubleSafe(apt1Other.value),
            ),
            apt2 = ExpenseDetails(
                electricity = toDoubleSafe(apt2Electricity.value),
                rent = toDoubleSafe(apt2Rent.value),
                gas = toDoubleSafe(apt2Gas.value),
                other = toDoubleSafe(apt2Other.value),
            ),
            car = CarExpense(
                fuel = toDoubleSafe(fuel.value),
                mileage = toIntSafe(mileage.value)
            )
        )

        storage.addOrUpdateExpense(entry)
        Toast.makeText(context, "Zapisano dane dla ${date.value}", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val dateDialogShown = remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Data: ${date.value} (${day.value})", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(16.dp))
            Button(onClick = { dateDialogShown.value = true }) {
                Text("Wybierz datę")
            }
        }

        if (dateDialogShown.value) {
            DatePickerDialog(
                initialDate = today,
                onDateSelected = { selectedDate ->
                    date.value = selectedDate.format(dateFormatter)
                    day.value = selectedDate.format(dayFormatter).replaceFirstChar { it.uppercaseChar() }
                    dateDialogShown.value = false
                },
                onDismiss = { dateDialogShown.value = false }
            )
        }
        Spacer(Modifier.height(16.dp))

        Text("Mieszkanie 1", style = MaterialTheme.typography.titleSmall)
        ExpenseInputs(apt1Electricity, apt1Rent, apt1Gas, apt1Other)

        Spacer(Modifier.height(16.dp))
        Text("Mieszkanie 2", style = MaterialTheme.typography.titleSmall)
        ExpenseInputs(apt2Electricity, apt2Rent, apt2Gas, apt2Other)

        Spacer(Modifier.height(16.dp))
        Text("Samochód", style = MaterialTheme.typography.titleSmall)
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
            label = { Text("Przebieg (km)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { saveData() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zapisz")
        }
    }
}

@Composable
fun ExpenseInputs(
    electricity: MutableState<String>,
    rent: MutableState<String>,
    gas: MutableState<String>,
    other: MutableState<String>
) {
    OutlinedTextField(
        value = electricity.value,
        onValueChange = { electricity.value = it },
        label = { Text("Prąd (zł)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = rent.value,
        onValueChange = { rent.value = it },
        label = { Text("Czynsz (zł)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = gas.value,
        onValueChange = { gas.value = it },
        label = { Text("Gaz (zł)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = other.value,
        onValueChange = { other.value = it },
        label = { Text("Inne (zł)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}
