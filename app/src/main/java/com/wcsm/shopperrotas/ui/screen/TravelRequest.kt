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
import com.wcsm.shopperrotas.ui.components.TravelRequestForm
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.TravelViewModel

@Composable
fun TravelRequest(
    navController: NavController,
    travelViewModel: TravelViewModel = hiltViewModel()
) {
    val estimatedWithSuccess by travelViewModel.estimatedWithSuccess.collectAsStateWithLifecycle()
    val errorMessage by travelViewModel.errorMessage.collectAsStateWithLifecycle()

    BackHandler {
        travelViewModel.clearErrorMessage()
        navController.popBackStack()
    }

    LaunchedEffect(estimatedWithSuccess) {
        if(estimatedWithSuccess == true) {
            travelViewModel.resetEstimatedWithSuccess()
            navController.navigate(Screen.TravelOptions.route)
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

            TravelRequestForm(
                errorMessage = errorMessage
            ) { customerId, origin, destination ->
                travelViewModel.clearErrorMessage()
                travelViewModel.fetchRideEstimate(customerId, origin, destination)

                // FOR TEST
                // navController.navigate(Screen.TravelHistory.route)
            }
        }
    }
}

@Preview
@Composable
fun TravelRequestPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        TravelRequest(navController)
    }
}

