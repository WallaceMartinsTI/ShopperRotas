package com.wcsm.shopperrotas.ui.screen

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.RideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        installSplashScreen()

        setContent {
            ShopperRotasTheme(dynamicColor = false) {
                SetBarColor(PrimaryColor)

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
private fun AppNavigation(
    rideViewModel: RideViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            RideMainScreen(
                navController = navController,
                rideViewModel = rideViewModel
            )
        }

        composable(route = Screen.RideRequestScreen.route) {
            RideRequestScreen(
                navController = navController,
                rideViewModel = rideViewModel
            )
        }

        composable(route = Screen.RideOptionsScreen.route) {
            RideOptionsScreen(
                navController = navController,
                rideViewModel = rideViewModel
            )
        }
        composable(route = Screen.RideHistoryScreen.route) {
            RideHistoryScreen(
                navController = navController,
                rideViewModel = rideViewModel
            )
        }
    }
}

@Composable
fun SetBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = color)
    }
}


