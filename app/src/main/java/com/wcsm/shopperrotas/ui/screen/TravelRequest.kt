package com.wcsm.shopperrotas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.ui.components.TravelRequestForm
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.TravelViewModel
import kotlinx.coroutines.delay

@Composable
fun TravelRequest(
    navController: NavController,
    travelViewModel: TravelViewModel = viewModel()
) {
    val estimatedWithSuccess by travelViewModel.estimatedWithSuccess.collectAsStateWithLifecycle()
    val errorMessage by travelViewModel.errorMessage.collectAsStateWithLifecycle()

    var isClickEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(estimatedWithSuccess) {
        if(estimatedWithSuccess == true) {
            navController.navigate(Screen.TravelOptions.route)
        }
    }

    LaunchedEffect(isClickEnabled) {
        if(!isClickEnabled) {
            delay(2000)
            isClickEnabled = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SOLICITAR VIAGEM",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Preencha os campos abaixo para estimar o valor da viagem!",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(350.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TravelRequestForm(
                isSubmitButtonEnabled = isClickEnabled,
                errorMessage = errorMessage
            ) { customerId, origin, destination ->
                if(isClickEnabled) {
                    isClickEnabled = false
                    travelViewModel.fetchRideEstimate(customerId, origin, destination)
                }
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

