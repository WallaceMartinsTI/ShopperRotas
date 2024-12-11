package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.api.ShopperAPI
import com.wcsm.shopperrotas.data.dto.ConfirmRideRequest
import com.wcsm.shopperrotas.data.dto.ConfirmRideResponse
import com.wcsm.shopperrotas.data.dto.Location
import com.wcsm.shopperrotas.data.dto.Review
import com.wcsm.shopperrotas.data.dto.RideEstimateRequest
import com.wcsm.shopperrotas.data.dto.RideEstimateResponse
import com.wcsm.shopperrotas.data.dto.RideOption
import com.wcsm.shopperrotas.data.dto.RideResponse
import retrofit2.Response
import javax.inject.Inject

class RideRepositoryImpl @Inject constructor(
    private val shopperAPI: ShopperAPI
) : IRideRepository {
    override suspend fun estimate(estimateRequest: RideEstimateRequest): RideEstimateResponse {
        return shopperAPI.getRideEstimate(estimateRequest)
    }

    override suspend fun confirm(confirmRideRequest: ConfirmRideRequest): Response<ConfirmRideResponse> {
        return shopperAPI.confirmRide(confirmRideRequest)
    }

    override suspend fun ride(customerId: String, driverId: Int?): Response<RideResponse> {
        return shopperAPI.getHistoryRides(customerId, driverId)
    }

}