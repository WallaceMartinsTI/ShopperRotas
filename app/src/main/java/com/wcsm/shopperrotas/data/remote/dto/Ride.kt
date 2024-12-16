package com.wcsm.shopperrotas.data.model

import com.wcsm.shopperrotas.data.remote.dto.Driver

data class Ride(
    val id: Int,
    val date: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: Driver,
    val value: Double
)
