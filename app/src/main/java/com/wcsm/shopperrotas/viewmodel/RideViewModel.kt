package com.wcsm.shopperrotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.shopperrotas.data.dto.ConfirmRideRequest
import com.wcsm.shopperrotas.data.dto.ConfirmRideResponse
import com.wcsm.shopperrotas.data.dto.Driver
import com.wcsm.shopperrotas.data.dto.Ride
import com.wcsm.shopperrotas.data.dto.RideEstimateRequest
import com.wcsm.shopperrotas.data.dto.RideEstimateResponse
import com.wcsm.shopperrotas.data.dto.RideOption
import com.wcsm.shopperrotas.data.repository.IRideRepository
import com.wcsm.shopperrotas.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideRepository : IRideRepository
) : ViewModel() {
    private val _estimateResponse = MutableStateFlow<RideEstimateResponse?>(null)
    val estimateResponse: StateFlow<RideEstimateResponse?> = _estimateResponse.asStateFlow()

    private val _drivers = MutableStateFlow<List<RideOption>?>(null)
    val drivers: StateFlow<List<RideOption>?> = _drivers.asStateFlow()

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

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

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

    fun setActionLoading(isLoading: Boolean) {
        _isActionLoading.value = isLoading
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = rideRepository.estimate(rideRequest)
                _requestRideData.value = rideRequest
                _estimateResponse.value = response
                _estimatedWithSuccess.value = true
                _drivers.value = response.options
                _errorMessage.value = null
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorDescription = errorBody?.let {
                    JSONObject(it).optString("error_description")
                }
                _errorMessage.value = errorDescription ?: "Ocorreu um erro, tente mais tarde."
                _estimateResponse.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Ocorreu um erro desconhecido."
                _estimateResponse.value = null
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    fun sendConfirmRide(confirmRideRequest: ConfirmRideRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = rideRepository.confirm(confirmRideRequest)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _confirmRideResponse.value = it
                        _errorMessage.value = null
                    } ?: run {
                        _confirmRideResponse.value = null
                        _errorMessage.value = "Erro desconhecido."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorDescription = errorBody?.let {
                        JSONObject(it).optString("error_description")
                    }
                    _errorMessage.value = errorDescription ?: "Erro desconhecido."
                    _confirmRideResponse.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Ocorreu um erro desconhecido."
                _confirmRideResponse.value = null
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    fun fetchRidesHistory(customerId: String, driverId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = rideRepository.ride(customerId, driverId)

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
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    // Filtering by ID doesn't work
    fun getFilteredRideHistory(customerId: String, driver: Driver) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = rideRepository.ride(customerId, driver.id)

                if (response.isSuccessful) {
                    _ridesHistory.value = response.body()?.rides?.filter {
                        it.driver.name == driver.name
                    }
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
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

}