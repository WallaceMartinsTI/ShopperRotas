package com.wcsm.shopperrotas.data.model

sealed class RideResponseState<out T> {
    class Success<T>(var data: T) : RideResponseState<T>()
    class Error(var errorMessage: String) : RideResponseState<Nothing>()
}