package com.wcsm.shopperrotas.ui.model

data class Travel(
    val dateTime: String,
    val driverName: String,
    val originAddress: String,
    val destinationAddress: String,
    val time: String,
    val value: Double
)
