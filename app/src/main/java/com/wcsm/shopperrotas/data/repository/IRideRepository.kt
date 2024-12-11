package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.dto.ConfirmRideRequest
import com.wcsm.shopperrotas.data.dto.ConfirmRideResponse
import com.wcsm.shopperrotas.data.dto.RideEstimateRequest
import com.wcsm.shopperrotas.data.dto.RideEstimateResponse
import com.wcsm.shopperrotas.data.dto.RideResponse
import retrofit2.Response

interface IRideRepository {

    suspend fun estimate(estimateRequest: RideEstimateRequest) : RideEstimateResponse
    suspend fun confirm(confirmRideRequest: ConfirmRideRequest) : Response<ConfirmRideResponse>
    suspend fun ride(customerId: String, driverId: Int?) : Response<RideResponse>

}