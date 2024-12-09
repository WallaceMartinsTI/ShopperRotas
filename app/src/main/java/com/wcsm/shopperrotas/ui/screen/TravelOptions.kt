package com.wcsm.shopperrotas.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.Driver
import com.wcsm.shopperrotas.ui.components.DriversAvailable
import com.wcsm.shopperrotas.ui.components.DynamicMap
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ErrorColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.utils.decodePolyline
import com.wcsm.shopperrotas.utils.getMinutesAndSeconds
import com.wcsm.shopperrotas.utils.revertGetMinutesAndSeconds
import com.wcsm.shopperrotas.viewmodel.TravelViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun TravelOptions(
    navController: NavController,
    travelViewModel: TravelViewModel = viewModel()
) {
    val TAG = "#-# TESTE #-#"

    val drivers by travelViewModel.drivers.collectAsStateWithLifecycle()
    val estimateResponse by travelViewModel.estimateResponse.collectAsStateWithLifecycle()
    val requestRideData by travelViewModel.requestRideData.collectAsStateWithLifecycle()
    val confirmRideResponse by travelViewModel.confirmRideResponse.collectAsStateWithLifecycle()
    val errorMessage by travelViewModel.errorMessage.collectAsStateWithLifecycle()

    val duration by remember { mutableStateOf(estimateResponse?.duration?.getMinutesAndSeconds() ?: "00:00") }
    val distance by remember { mutableStateOf(estimateResponse?.distance?.toString()) }

    var originLatitude: Double? by remember { mutableStateOf(null) }
    var originLongitude: Double? by remember { mutableStateOf(null) }
    var destinationLatitude: Double? by remember { mutableStateOf(null) }
    var destinationLongitude: Double? by remember { mutableStateOf(null) }

    var isPositionValid by remember { mutableStateOf(false) }

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(estimateResponse) {
        estimateResponse.let {
            originLatitude = estimateResponse?.origin?.latitude
            originLongitude = estimateResponse?.destination?.longitude
            destinationLatitude = estimateResponse?.destination?.latitude
            destinationLongitude = estimateResponse?.destination?.longitude

            isPositionValid = travelViewModel.validateLatLongForGoogleMaps(
                originLatitude = originLatitude,
                originLongitude = originLongitude,
                destinationLatitude = destinationLatitude,
                destinationLongitude = destinationLongitude
            )
        }
    }

    LaunchedEffect(confirmRideResponse) {
        if(confirmRideResponse?.success == true) {
            navController.navigate(Screen.TravelHistory.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OPÇÕES DE VIAGEM",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if(isPositionValid) {
                    val originLatLnt = LatLng(originLatitude!!, originLongitude!!)
                    val destinationLatLnt = LatLng(destinationLatitude!!, destinationLongitude!!)
                    DynamicMap(originLatLnt, destinationLatLnt)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOff,
                            contentDescription = "Icone de localização não encontrada",
                            tint = TertiaryColor,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Mapa indisponível quando nenhum motorista é encontrado.",
                            color = TertiaryColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(280.dp)
                        )
                    }
                }
            }

            if(!errorMessage.isNullOrEmpty()) {
                Text(
                    text = "Erro: ${errorMessage!!}",
                    color = ErrorColor
                )
            }

            if(isPositionValid) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Duração: $duration min")
                    Text("Distância: $distance")
                }
            } else {
                HorizontalDivider(color = Color.White)
            }

            DriversAvailable(drivers) { driver ->
                travelViewModel.clearErrorMessage()
                // When user select a driver
                Log.i(TAG, "distance: $distance")
                Log.i(TAG, "distance?.toDoubleOrNull() ?: 1.0: ${distance?.toDoubleOrNull() ?: 1.0}")

                val confirmRide = ConfirmRideRequest(
                    customer_id = requestRideData?.customer_id ?: "",
                    origin = requestRideData?.origin ?: "",
                    destination = requestRideData?.destination ?: "",
                    distance = distance?.toDoubleOrNull() ?: 1.0,
                    duration = duration.revertGetMinutesAndSeconds(),
                    driver = Driver(driver.id, driver.name),
                    value = driver.value
                )

                travelViewModel.sendConfirmRide(confirmRide)
            }
        }
    }
}

@Preview
@Composable
fun TravelOptionsPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        TravelOptions(navController = navController)
    }
}