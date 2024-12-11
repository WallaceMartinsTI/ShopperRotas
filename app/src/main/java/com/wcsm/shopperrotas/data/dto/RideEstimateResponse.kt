package com.wcsm.shopperrotas.data.dto

data class RideEstimateResponse(
    val origin: Location,
    val destination: Location,
    val distance: Int,
    val duration: String,
    val options: List<RideOption>,
    val routeResponse: Any
)
