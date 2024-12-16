package com.wcsm.shopperrotas.data.model

import com.wcsm.shopperrotas.data.remote.dto.Driver

data class RideConfirmRequest(
    val customer_id: String,
    val origin: String,
    val destination: String,
    val distance: Int,
    val duration: String,
    val driver: Driver,
    val value: Double
)
