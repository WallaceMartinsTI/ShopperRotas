package com.wcsm.shopperrotas.data.model

sealed class RideEstimateResponse<out T> {
    class Success<T>(var data: T) : RideEstimateResponse<T>()
    class Error(var errorMessage: String) : RideEstimateResponse<Nothing>()
}