package com.wcsm.shopperrotas.ui.model

sealed class Screen(val route: String) {
    data object MainScreen : Screen("ride_main")
    data object RideRequestScreen : Screen("ride_request")
    data object RideOptionsScreen : Screen("ride_options")
    data object RideHistoryScreen : Screen("ride_history")
}
