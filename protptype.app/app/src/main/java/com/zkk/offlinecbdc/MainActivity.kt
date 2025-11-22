package com.zkk.offlinecbdc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zkk.offlinecbdc.ui.NavRoutes
import com.zkk.offlinecbdc.ui.screens.*
import com.zkk.offlinecbdc.ui.splash.SplashScreen
import com.zkk.offlinecbdc.ui.theme.OfflineCBDCTheme
import com.zkk.offlinecbdc.viewmodel.TokenViewModel

class MainActivity : ComponentActivity() {

    // 🔥 THIS ensures ONE shared ViewModel for whole app
    private val sharedViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OfflineCBDCTheme {

                val navController = rememberNavController()

                OfflineCBDCApp(
                    navController = navController,
                    vm = sharedViewModel
                )
            }
        }
    }
}

@Composable
fun OfflineCBDCApp(
    navController: androidx.navigation.NavHostController,
    vm: TokenViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) { SplashScreen(navController) }
        composable(NavRoutes.HOME) { HomeScreen(navController, vm) }
        composable(NavRoutes.PAY) { PayScreen(navController, vm) }
        composable(NavRoutes.SCAN) { ScanScreen(navController, vm) }
        composable(NavRoutes.ISSUE) { IssueNotesScreen(navController, vm) }
        composable(NavRoutes.BALANCE) { BalanceScreen(navController, vm) }
    }
}
