package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.model.RideResponseState
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate

interface IRideRepository {

    suspend fun estimate(estimateRequest: RideEstimateRequest) : RideResponseState<RideEstimate>
    suspend fun confirm(rideConfirmRequest: RideConfirmRequest) : RideResponseState<RideConfirmResponse>
    suspend fun ride(rideRequest: RideRequest) : RideResponseState<RideResponse>

}