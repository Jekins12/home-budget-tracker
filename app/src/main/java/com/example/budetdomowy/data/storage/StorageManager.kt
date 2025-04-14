package com.example.budetdomowy.data.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.budetdomowy.data.model.DailyExpense
import java.io.File

class StorageManager(private val context: Context) {

    private val fileName = "expenses_2025_04.json"
    private val gson = Gson()

    fun loadExpenses(): List<DailyExpense> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<DailyExpense>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveExpenses(expenses: List<DailyExpense>) {
        val file = File(context.filesDir, fileName)
        val json = gson.toJson(expenses)
        file.writeText(json)
    }

    fun addOrUpdateExpense(expense: DailyExpense) {
        val allExpenses = loadExpenses().toMutableList()
        val index = allExpenses.indexOfFirst { it.date == expense.date }

        if (index >= 0) {
            allExpenses[index] = expense
        } else {
            allExpenses.add(expense)
        }

        saveExpenses(allExpenses)
    }

    fun deleteExpenseByDate(date: String) {
        val updated = loadExpenses().filterNot { it.date == date }
        saveExpenses(updated)
    }
}