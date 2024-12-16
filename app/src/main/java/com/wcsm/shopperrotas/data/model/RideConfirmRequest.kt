package com.wcsm.shopperrotas.data.model

import com.google.gson.annotations.SerializedName
import com.wcsm.shopperrotas.data.remote.dto.Driver

data class RideConfirmRequest(
    @SerializedName("customer_id")
    val customerId: String?,
    val origin: String,
    val destination: String,
    val distance: Int,
    val duration: String,
    val driver: Driver,
    val value: Double
)
