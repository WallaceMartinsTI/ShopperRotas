package com.wcsm.shopperrotas.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.ui.components.RideRequestForm
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.RideViewModel

@Composable
fun RideRequestScreen(
    navController: NavController,
    rideViewModel: RideViewModel
) {
    val estimatedWithSuccess by rideViewModel.estimatedWithSuccess.collectAsStateWithLifecycle()
    val isActionLoading by rideViewModel.isActionLoading.collectAsStateWithLifecycle()
    val errorMessage by rideViewModel.errorMessage.collectAsStateWithLifecycle()

    BackHandler {
        rideViewModel.clearErrorMessage()
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        rideViewModel.setActionLoading(false)
    }

    LaunchedEffect(estimatedWithSuccess) {
        if(estimatedWithSuccess == true) {
            rideViewModel.resetEstimatedWithSuccess()
            navController.navigate(Screen.RideOptionsScreen.route)
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
                text = "SOLICITAR VIAGEM",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Preencha os campos abaixo para estimar o valor da viagem!",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(350.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            RideRequestForm(
                isActionLoading = isActionLoading,
                errorMessage = errorMessage
            ) { customerId, origin, destination ->
                rideViewModel.setActionLoading(true)
                rideViewModel.clearErrorMessage()
                rideViewModel.fetchRideEstimate(customerId, origin, destination)

                // FOR TEST
                // navController.navigate(Screen.TravelHistory.route)
            }
        }
    }
}

@Preview
@Composable
fun RideRequestScreenPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        RideRequestScreen(navController, hiltViewModel())
    }
}

