package com.wcsm.shopperrotas.ui.model

sealed class Screen(val route: String) {
    data object MainScreen : Screen("ride_main")
    data object RideRequest : Screen("ride_request")
    data object RideOptions : Screen("ride_options")
    data object RideHistory : Screen("ride_history")
}
