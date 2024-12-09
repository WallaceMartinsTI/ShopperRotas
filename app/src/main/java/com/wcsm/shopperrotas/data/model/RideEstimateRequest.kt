package com.wcsm.shopperrotas.data.model

data class RideEstimateRequest(
    val customer_id: String,
    val origin: String,
    val destination: String
)