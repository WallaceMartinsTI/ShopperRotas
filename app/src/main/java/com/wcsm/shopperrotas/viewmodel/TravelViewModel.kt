package com.wcsm.shopperrotas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.shopperrotas.data.api.RetrofitService
import com.wcsm.shopperrotas.data.api.ShopperAPI
import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.ConfirmRideResponse
import com.wcsm.shopperrotas.data.model.Ride
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideEstimateResponse
import com.wcsm.shopperrotas.data.model.RideOption
import com.wcsm.shopperrotas.data.repository.ITravelRepository
import com.wcsm.shopperrotas.data.repository.TravelRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class TravelViewModel(
    //private val getTravelInformationRepository : IGetTravelInfoRepository
) : ViewModel() {
    private val shopperAPI = RetrofitService.getAPI(ShopperAPI::class.java)
    private val travelRepository:
            ITravelRepository = TravelRepositoryImpl(shopperAPI)

    val TAG = "#-# TESTE #-#"

    private val _estimateResponse = MutableStateFlow<RideEstimateResponse?>(null)
    val estimateResponse: StateFlow<RideEstimateResponse?> = _estimateResponse.asStateFlow()

    private val _drivers = MutableStateFlow<List<RideOption>?>(null)
    val drivers: StateFlow<List<RideOption>??> = _drivers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _estimatedWithSuccess = MutableStateFlow<Boolean?>(null)
    val estimatedWithSuccess: StateFlow<Boolean?> = _estimatedWithSuccess.asStateFlow()

    private val _confirmRideResponse = MutableStateFlow<ConfirmRideResponse?>(null)
    val confirmRideResponse: StateFlow<ConfirmRideResponse?> = _confirmRideResponse.asStateFlow()

    private val _requestRideData = MutableStateFlow<RideEstimateRequest?>(null)
    val requestRideData: StateFlow<RideEstimateRequest?> = _requestRideData.asStateFlow()

    private val _ridesHistory = MutableStateFlow<List<Ride>?>(null)
    val ridesHistory: StateFlow<List<Ride>?> = _ridesHistory.asStateFlow()

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearRideHistory() {
        _ridesHistory.value = null
    }

    fun resetEstimatedWithSuccess() {
        _estimatedWithSuccess.value = false
    }

    fun resetConfirmRideResponse() {
        _confirmRideResponse.value = null
    }

    fun validateLatLongForGoogleMaps(
        originLatitude: Double?,
        originLongitude: Double?,
        destinationLatitude: Double?,
        destinationLongitude: Double?
    ): Boolean {
        val isNotNull = originLatitude != null && originLongitude != null &&
        destinationLatitude != null && destinationLongitude != null

        val isNotZero = originLatitude != 0.0 && originLongitude != 0.0 &&
                destinationLatitude != 0.0 && destinationLongitude != 0.0

        return isNotNull && isNotZero
    }

    fun fetchRideEstimate(customerId: String, origin: String, destination: String) {
        _estimatedWithSuccess.value = false
        val rideRequest = RideEstimateRequest(
            customer_id = customerId.ifBlank { null },
            origin = origin.ifBlank { null },
            destination = destination.ifBlank { null }
        )

        Log.i(TAG, "rideRequest: $rideRequest")

        viewModelScope.launch {
            try {
                val response = travelRepository.estimate(rideRequest)
                Log.i(TAG, "response: $response")
                _requestRideData.value = rideRequest
                _estimateResponse.value = response
                _estimatedWithSuccess.value = true
                _drivers.value = response.options
                _errorMessage.value = null
            } catch (e: HttpException) {
                Log.i(TAG, "e1: $e")
                val errorBody = e.response()?.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    JSONObject(it).optString("error_description")
                }
                _errorMessage.value = errorDescription ?: "Ocorreu um erro, tente mais tarde."
                _estimateResponse.value = null
            } catch (e: Exception) {
                Log.i(TAG, "e2: $e")
                _errorMessage.value = e.localizedMessage ?: "Ocorreu um erro desconhecido."
                _estimateResponse.value = null
            }
        }
    }

    fun sendConfirmRide(confirmRideRequest: ConfirmRideRequest) {
        viewModelScope.launch {
            try {
                val response = travelRepository.confirm(confirmRideRequest)
                if(response.success == true) {
                    _confirmRideResponse.value = response
                    _errorMessage.value = null
                } else {
                    _confirmRideResponse.value = null
                    _errorMessage.value = response.errorDescription ?: "Erro desconhecido."
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    JSONObject(it).optString("error_description")
                }
                _errorMessage.value = errorDescription ?: "Ocorreu um erro, tente mais tarde."
                _confirmRideResponse.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Ocorreu um erro desconhecido."
                _confirmRideResponse.value = null
            }
        }
    }

    fun fetchRidesHistory(customerId: String, driverId: Int?) {
        viewModelScope.launch {
            try {
                val response = shopperAPI.getHistoryRides(customerId, driverId)

                if (response.isSuccessful) {
                    _ridesHistory.value = response.body()?.rides
                    _errorMessage.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorDescription = errorBody?.let {
                        JSONObject(it).optString("error_description")
                    }
                    _errorMessage.value = errorDescription ?: "Erro desconhecido."
                }
            } catch (e: HttpException) {
                _errorMessage.value = "Ocorreu um erro, tente mais tarde."
            } catch (e: Exception) {
                _errorMessage.value = "Ocorreu um erro desconhecido."
            }
        }
    }

}