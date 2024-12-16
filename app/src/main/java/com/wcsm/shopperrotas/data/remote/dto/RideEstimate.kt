package com.wcsm.shopperrotas.data.remote.dto

data class RideEstimate(
    val origin: Location,
    val destination: Location,
    val distance: Int,
    val duration: String,
    val options: List<RideOption>,
    val routeResponse: Any
)
