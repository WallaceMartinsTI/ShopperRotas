package com.wcsm.shopperrotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.model.RideResponseState
import com.wcsm.shopperrotas.data.remote.dto.Driver
import com.wcsm.shopperrotas.data.remote.dto.Ride
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import com.wcsm.shopperrotas.data.remote.dto.RideOption
import com.wcsm.shopperrotas.data.repository.IRideRepository
import com.wcsm.shopperrotas.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor(
    private val rideRepository : IRideRepository
) : ViewModel() {
    private val _estimateResponse = MutableStateFlow<RideEstimate?>(null)
    val estimateResponse: StateFlow<RideEstimate?> = _estimateResponse.asStateFlow()

    private val _drivers = MutableStateFlow<List<RideOption>?>(null)
    val drivers: StateFlow<List<RideOption>?> = _drivers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _estimatedWithSuccess = MutableStateFlow<Boolean?>(null)
    val estimatedWithSuccess: StateFlow<Boolean?> = _estimatedWithSuccess.asStateFlow()

    private val _rideConfirmed = MutableStateFlow(false)
    val rideConfirmed: StateFlow<Boolean> = _rideConfirmed.asStateFlow()

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
        _rideConfirmed.value = false
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
        val rideEstimateRequest = RideEstimateRequest(
            customerId = customerId.ifBlank { null },
            origin = origin.ifBlank { null },
            destination = destination.ifBlank { null }
        )

        viewModelScope.launch {
            try {
                when(val rideEstimateResponse = rideRepository.estimate(rideEstimateRequest)) {
                    is RideResponseState.Success -> {
                        _requestRideData.value = rideEstimateRequest
                        _estimateResponse.value = rideEstimateResponse.data
                        _estimatedWithSuccess.value = true
                        _drivers.value = rideEstimateResponse.data.options
                        _errorMessage.value = null
                    }
                    is RideResponseState.Error -> {
                        _errorMessage.value = rideEstimateResponse.errorMessage
                        _estimateResponse.value = null
                    }
                }
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    fun sendConfirmRide(rideConfirmRequest: RideConfirmRequest) {
        viewModelScope.launch {
            try {
                when(val rideConfirmResponse = rideRepository.confirm(rideConfirmRequest)) {
                    is RideResponseState.Success -> {
                        _rideConfirmed.value = rideConfirmResponse.data.success
                        _errorMessage.value = null
                    }
                    is RideResponseState.Error -> {
                        _rideConfirmed.value = false
                        _errorMessage.value = rideConfirmResponse.errorMessage
                    }
                }
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    fun fetchRidesHistory(customerId: String, driverId: Int?) {
        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driverId
        )

        viewModelScope.launch {
            try {
                when(val rideHistoryResponse = rideRepository.ride(rideRequest)) {
                    is RideResponseState.Success -> {
                        _ridesHistory.value = rideHistoryResponse.data.rides
                        _errorMessage.value = null
                    }
                    is RideResponseState.Error -> {
                        _errorMessage.value = rideHistoryResponse.errorMessage
                    }
                }
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

    // Filtering by ID doesn't work
    fun getFilteredRideHistory(customerId: String, driver: Driver) {
        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driver.id
        )

        viewModelScope.launch {
            try {
                when(val rideHistoryResponse = rideRepository.ride(rideRequest)) {
                    is RideResponseState.Success -> {
                        _ridesHistory.value = rideHistoryResponse.data.rides?.filter {
                            it.driver.name == driver.name
                        }
                        _errorMessage.value = null
                    }
                    is RideResponseState.Error -> {
                        _errorMessage.value = rideHistoryResponse.errorMessage
                    }
                }
            } finally {
                delay(Constants.CLICK_DELAY)
                _isActionLoading.value = false
            }
        }
    }

}