package com.wcsm.shopperrotas.data.dto

data class ConfirmRideRequest(
    val customer_id: String,
    val origin: String,
    val destination: String,
    val distance: Int,
    val duration: String,
    val driver: Driver,
    val value: Double
)
