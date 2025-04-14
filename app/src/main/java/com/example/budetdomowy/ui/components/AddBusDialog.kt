package com.example.budetdomowy.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp

@Composable
fun AddBusDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var day by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj przejazd") },
        text = {
            Column {
                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it },
                    label = { Text("Dzień (np. 14)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Numer autobusu") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Godzina (np. 21:32)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (day.isNotBlank() && number.isNotBlank() && time.isNotBlank()) {
                        onAdd(day, number, time)
                    }
                }
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
