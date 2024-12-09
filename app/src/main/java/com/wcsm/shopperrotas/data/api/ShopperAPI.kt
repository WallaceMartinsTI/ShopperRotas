package com.wcsm.shopperrotas.data.api

import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.ConfirmRideResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideEstimateResponse
import com.wcsm.shopperrotas.data.model.RideResponse
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
    ) : RideEstimateResponse

    @PATCH("/ride/confirm")
    suspend fun confirmRide(
        @Body confirmRideRequest: ConfirmRideRequest
    ) : Response<ConfirmRideResponse>

    @GET("/ride/{customer_id}")
    suspend fun getHistoryRides(
        @Path("customer_id") customerId: String,
        @Query("driver_id") driverId: Int?
    ) : Response<RideResponse>
}