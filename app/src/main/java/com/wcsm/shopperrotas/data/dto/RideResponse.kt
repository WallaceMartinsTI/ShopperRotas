package com.wcsm.shopperrotas.data.dto

data class RideResponse(
    val customer_id: String,
    val rides: List<Ride>?
)
