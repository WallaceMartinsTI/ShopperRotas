package com.wcsm.shopperrotas.data.repository

import com.wcsm.shopperrotas.data.api.ShopperAPI
import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.ConfirmRideResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideEstimateResponse
import com.wcsm.shopperrotas.data.model.RideResponse
import retrofit2.HttpException
import retrofit2.Response

class TravelRepositoryImpl(
    private val shopperAPI: ShopperAPI
) : ITravelRepository {
    override suspend fun estimate(estimateRequest: RideEstimateRequest): RideEstimateResponse {
        return shopperAPI.getRideEstimate(estimateRequest)
    }

    override suspend fun confirm(confirmRideRequest: ConfirmRideRequest): ConfirmRideResponse {
        val response = shopperAPI.confirmRide(confirmRideRequest)
        if (response.isSuccessful) {
            return response.body() ?: ConfirmRideResponse(errorDescription = "Erro desconhecido.")
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun ride(customerId: String, driverId: Int?): Response<RideResponse> {
        return shopperAPI.getHistoryRides(customerId, driverId)
    }

}