package com.wcsm.shopperrotas.data.dto

data class RideEstimateResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<RideOption>,
    val routeResponse: Any
)
