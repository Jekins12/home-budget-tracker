package com.example.budetdomowy.data.model

data class DailyExpense(
    val date: String,
    val dayOfWeek: String,
    val apt1: ExpenseDetails = ExpenseDetails(),
    val apt2: ExpenseDetails = ExpenseDetails(),
    val car: CarExpense = CarExpense(),
    val buses: List<BusRide> = emptyList()
)

data class ExpenseDetails(
    val electricity: Double = 0.0,
    val rent: Double = 0.0,
    val gas: Double = 0.0,
    val other: Double = 0.0
)

data class CarExpense(
    val fuel: Double = 0.0,
    val mileage: Int = 0
)

data class BusRide(
    val busNumber: String,
    val arrivalTime: String
)