package com.wcsm.shopperrotas.data.model

import com.wcsm.shopperrotas.data.remote.dto.Review

data class RideOption(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
)
