package com.wcsm.shopperrotas.data.remote.api

import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopperAPI {

    @POST("/ride/estimate")
    suspend fun getRideEstimate(
        @Body estimateRequest: RideEstimateRequest
    ) : Response<RideEstimate>

    @PATCH("/ride/confirm")
    suspend fun confirmRide(
        @Body rideConfirmRequest: RideConfirmRequest
    ) : Response<RideConfirmResponse>

    @GET("/ride/{customer_id}")
    suspend fun getHistoryRides(
        @Path("customer_id") customerId: String,
        @Query("driver_id") driverId: Int?
    ) : Response<RideResponse>
}