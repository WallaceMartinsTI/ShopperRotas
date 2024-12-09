package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.ConfirmRideResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideEstimateResponse
import com.wcsm.shopperrotas.data.model.RideResponse
import retrofit2.Response

interface ITravelRepository {

    suspend fun estimate(estimateRequest: RideEstimateRequest) : RideEstimateResponse
    suspend fun confirm(confirmRideRequest: ConfirmRideRequest) : ConfirmRideResponse
    suspend fun ride(customerId: String, driverId: Int?) : Response<RideResponse>

}