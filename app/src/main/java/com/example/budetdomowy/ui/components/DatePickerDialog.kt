package com.example.budetdomowy.ui.components

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val year = initialDate.year
    val month = initialDate.monthValue - 1 // Java Calendar is 0-based
    val day = initialDate.dayOfMonth

    DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            onDateSelected(LocalDate.of(y, m + 1, d))
        },
        year, month, day
    ).apply {
        setOnCancelListener { onDismiss() }
        show()
    }
}
