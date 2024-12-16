package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.model.RideEstimateResponse
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import retrofit2.Response

interface IRideRepository {

    suspend fun estimate(estimateRequest: RideEstimateRequest) : RideEstimateResponse<RideEstimate>
    suspend fun confirm(rideConfirmRequest: RideConfirmRequest) : RideEstimateResponse<RideConfirmResponse>
    suspend fun ride(rideRequest: RideRequest) : RideEstimateResponse<RideResponse>

}