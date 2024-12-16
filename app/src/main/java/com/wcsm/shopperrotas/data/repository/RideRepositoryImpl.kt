package com.wcsm.shopperrotas.data.repository

import com.google.gson.JsonParser
import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.model.RideResponseState
import com.wcsm.shopperrotas.data.remote.api.ShopperAPI
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import javax.inject.Inject

class RideRepositoryImpl @Inject constructor(
    private val shopperAPI: ShopperAPI
) : IRideRepository {
    override suspend fun estimate(
        estimateRequest: RideEstimateRequest
    ): RideResponseState<RideEstimate> {
        return try {
            val response = shopperAPI.getRideEstimate(estimateRequest)
            if(response.isSuccessful && response.body() != null) {
                RideResponseState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    val jsonObject = JsonParser.parseString(it).asJsonObject
                    jsonObject.get("error_description")?.asString
                }
                RideResponseState.Error(errorDescription ?: "Ocorreu um erro, tente mais tarde.")
            }
        } catch (e: Exception) {
            RideResponseState.Error(e.localizedMessage ?: "Ocorreu um erro desconhecido.")
        }
    }

    override suspend fun confirm(
        rideConfirmRequest: RideConfirmRequest
    ): RideResponseState<RideConfirmResponse> {
        return try {
            val response = shopperAPI.confirmRide(rideConfirmRequest)
            if(response.isSuccessful && response.body() != null) {
                RideResponseState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    val jsonObject = JsonParser.parseString(it).asJsonObject
                    jsonObject.get("error_description")?.asString
                }
                RideResponseState.Error(errorDescription ?: "Ocorreu um erro, tente mais tarde.")
            }
        } catch (e: Exception) {
            RideResponseState.Error(e.localizedMessage ?: "Ocorreu um erro desconhecido.")
        }
    }

    override suspend fun ride(
        rideRequest: RideRequest
    ): RideResponseState<RideResponse> {
        return try {
            val customerId = rideRequest.customerId
            val driverId = rideRequest.driverId

            val response = shopperAPI.getHistoryRides(customerId, driverId)
            if(response.isSuccessful && response.body() != null) {
                RideResponseState.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    val jsonObject = JsonParser.parseString(it).asJsonObject
                    jsonObject.get("error_description")?.asString
                }
                RideResponseState.Error(errorDescription ?: "Ocorreu um erro, tente mais tarde.")
            }
        } catch (e: Exception) {
            RideResponseState.Error(e.localizedMessage ?: "Ocorreu um erro desconhecido.")
        }
    }

}