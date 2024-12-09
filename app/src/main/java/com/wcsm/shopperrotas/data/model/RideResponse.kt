package com.wcsm.shopperrotas.data.model

data class RideResponse(
    val customer_id: String,
    val rides: List<Ride>?
)
