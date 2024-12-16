package com.wcsm.shopperrotas.data.model

import com.google.gson.annotations.SerializedName

data class RideEstimateRequest(
    @SerializedName("customer_id")
    val customerId: String?,
    val origin: String?,
    val destination: String?
)