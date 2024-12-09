package com.wcsm.shopperrotas.ui.screen

import android.util.Log
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.data.model.ConfirmRideRequest
import com.wcsm.shopperrotas.data.model.Driver
import com.wcsm.shopperrotas.ui.components.DriversAvailable
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ErrorColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.utils.getKmOrMeters
import com.wcsm.shopperrotas.utils.getMinutesAndSeconds
import com.wcsm.shopperrotas.utils.revertGetMinutesAndSeconds
import com.wcsm.shopperrotas.viewmodel.TravelViewModel
import kotlinx.coroutines.delay

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
    val distance by remember { mutableStateOf(estimateResponse?.distance?.getKmOrMeters() ?: "0") }

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
                    .border(1.dp, Color.Red)
            )

            if(!errorMessage.isNullOrEmpty()) {
                Text(
                    text = "Erro: ${errorMessage!!}",
                    color = ErrorColor
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Red),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Duração: $duration min")
                Text("Distância: $distance")
            }

            DriversAvailable(drivers) { driver ->
                travelViewModel.resetErrorMessage()
                // When user select a driver
                Log.i(TAG, "estimateResponse?.distance?.getKmOrMeters() ?: \"0\": ${estimateResponse?.distance?.getKmOrMeters() ?: "0"}")
                Log.i(TAG, "distance: $distance")
                Log.i(TAG, "distance.toIntOrNull() ?: 0: ${distance.toIntOrNull() ?: 0}")
                val confirmRide = ConfirmRideRequest(
                    customer_id = requestRideData?.customer_id ?: "",
                    origin = requestRideData?.origin ?: "",
                    destination = requestRideData?.destination ?: "",
                    distance = distance.toIntOrNull() ?: 1,
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