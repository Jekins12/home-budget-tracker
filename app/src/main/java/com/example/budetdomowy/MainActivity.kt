package com.example.budetdomowy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budetdomowy.ui.screens.BudgetScreen
import com.example.budetdomowy.ui.screens.BusScreen
import com.example.budetdomowy.ui.screens.DetailsScreen
import com.example.budetdomowy.ui.screens.EditDayScreen
import com.example.budetdomowy.ui.screens.MainScreen
import com.example.budetdomowy.ui.screens.SavedDaysScreen
import com.example.budetdomowy.ui.screens.SummaryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("budget") { BudgetScreen() }
        composable("bus") {
            BusScreen()
        }
        composable("saved") {
            SavedDaysScreen(navController)
        }
        composable("edit_day/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: return@composable
            EditDayScreen(date, navController)
        }
        composable("summary") {
            SummaryScreen(navController) // ✅ Pass it here
        }

        composable("details/{category}?month={month}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val month = backStackEntry.arguments?.getString("month")
            DetailsScreen(category, month)
        }

    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
