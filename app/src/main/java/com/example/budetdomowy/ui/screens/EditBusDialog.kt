package com.example.budetdomowy.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditBusDialog(
    ride: Triple<String, String, String>,
    onDismiss: () -> Unit,
    onUpdate: (String, String, String, String) -> Unit,
    onDelete: (String, String, String) -> Unit
) {
    var number by remember { mutableStateOf(ride.second) }
    var time by remember { mutableStateOf(ride.third) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj przejazd") },
        text = {
            Column {
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Numer autobusu") }
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Godzina") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdate(ride.first, number, time, ride.third)
            }) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDelete(ride.first, ride.second, ride.third) }) {
                    Text("Usuń")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Anuluj")
                }
            }
        }
    )
}
