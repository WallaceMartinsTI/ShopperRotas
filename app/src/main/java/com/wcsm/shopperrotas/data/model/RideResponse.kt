package com.wcsm.shopperrotas.data.model

import com.wcsm.shopperrotas.data.remote.dto.Ride

data class RideResponse(
    val customer_id: String,
    val rides: List<Ride>?
)
