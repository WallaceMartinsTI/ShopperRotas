package com.wcsm.shopperrotas.data.model

data class RideEstimateResponse(
    val origin: Location,
    val destination: Location,
    val distance: Int,
    val duration: String,
    val options: List<RideOption>,
    val routeResponse: Any
)
