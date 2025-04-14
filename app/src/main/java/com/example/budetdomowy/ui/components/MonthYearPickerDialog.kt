package com.example.budetdomowy.ui.components

import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun MonthYearPickerDialog(
    initialMonthYear: String,
    onMonthSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val parts = initialMonthYear.split(".")
    val month = (parts.getOrNull(0)?.toIntOrNull() ?: (calendar.get(Calendar.MONTH) + 1)) - 1
    val year = parts.getOrNull(1)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)

    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    val dialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, _: Int ->
            val selected = "%02d.%d".format(m + 1, y)
            onMonthSelected(selected)
        },
        year, month, 1 // 👈 day ignored
    )

    dialog.datePicker.findViewById<View>(
        context.resources.getIdentifier("day", "id", "android")
    )?.visibility = View.GONE

    dialog.setOnCancelListener { onDismiss() }
    dialog.show()
}
