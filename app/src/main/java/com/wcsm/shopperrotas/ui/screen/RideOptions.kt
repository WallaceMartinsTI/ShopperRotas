package com.wcsm.shopperrotas.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.Driver
import com.wcsm.shopperrotas.ui.components.DriversAvailable
import com.wcsm.shopperrotas.ui.components.DynamicMap
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ErrorColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.utils.getMinutesAndSeconds
import com.wcsm.shopperrotas.utils.revertGetMinutesAndSeconds
import com.wcsm.shopperrotas.viewmodel.RideViewModel

@Composable
fun RideOptions(
    navController: NavController,
    rideViewModel: RideViewModel = hiltViewModel()
) {
    val drivers by rideViewModel.drivers.collectAsStateWithLifecycle()
    val estimateResponse by rideViewModel.estimateResponse.collectAsStateWithLifecycle()
    val requestRideData by rideViewModel.requestRideData.collectAsStateWithLifecycle()
    val confirmRideResponse by rideViewModel.confirmRideResponse.collectAsStateWithLifecycle()
    val errorMessage by rideViewModel.errorMessage.collectAsStateWithLifecycle()

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

            isPositionValid = rideViewModel.validateLatLongForGoogleMaps(
                originLatitude = originLatitude,
                originLongitude = originLongitude,
                destinationLatitude = destinationLatitude,
                destinationLongitude = destinationLongitude
            )
        }
    }

    LaunchedEffect(confirmRideResponse) {
        if(confirmRideResponse?.success == true) {
            rideViewModel.resetConfirmRideResponse()
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
                style = MaterialTheme.typography.titleMedium,
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
                    //DynamicMap(originLatLnt, destinationLatLnt)
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
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(280.dp)
                        )
                    }
                }
            }

            if(!errorMessage.isNullOrEmpty()) {
                Text(
                    text = "Erro: ${errorMessage!!}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ErrorColor
                )
            }

            if(isPositionValid) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Duração: $duration",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Distância: $distance",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                HorizontalDivider(color = Color.White)
            }

            DriversAvailable(drivers) { driver ->
                rideViewModel.clearErrorMessage()

                val confirmRide = ConfirmRideRequest(
                    customer_id = requestRideData?.customer_id ?: "",
                    origin = requestRideData?.origin ?: "",
                    destination = requestRideData?.destination ?: "",
                    distance = distance?.toDoubleOrNull() ?: 1.0,
                    duration = duration.revertGetMinutesAndSeconds(),
                    driver = Driver(driver.id, driver.name),
                    value = driver.value
                )

                rideViewModel.sendConfirmRide(confirmRide)
            }
        }
    }
}

@Preview
@Composable
fun RideOptionsPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        RideOptions(navController = navController)
    }
}