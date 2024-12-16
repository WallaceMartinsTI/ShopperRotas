package com.wcsm.shopperrotas.data.model

import com.google.gson.annotations.SerializedName
import com.wcsm.shopperrotas.data.remote.dto.Ride

data class RideResponse(
    @SerializedName("customer_id")
    val customerId: String,
    val rides: List<Ride>?
)
