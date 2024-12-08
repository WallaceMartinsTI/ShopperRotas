package com.wcsm.shopperrotas.ui.model

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
    data object TravelRequest : Screen("travel_request")
    data object TravelOptions : Screen("travel_options")
    data object TravelHistory : Screen("travel_history")
}
