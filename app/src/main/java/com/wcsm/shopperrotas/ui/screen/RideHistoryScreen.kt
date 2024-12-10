package com.wcsm.shopperrotas.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.ui.components.RideHisotryFilter
import com.wcsm.shopperrotas.ui.components.RideHistoryCard
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.RideViewModel

@Composable
fun RideHistoryScreen(
    navController: NavController,
    rideViewModel: RideViewModel
) {
    val riderHistory by rideViewModel.ridesHistory.collectAsStateWithLifecycle()
    val errorMessage by rideViewModel.errorMessage.collectAsStateWithLifecycle()
    val isActionLoading by rideViewModel.isActionLoading.collectAsStateWithLifecycle()

    BackHandler {
        rideViewModel.clearRideHistory()
        navController.navigate(Screen.MainScreen.route)
    }

    LaunchedEffect(Unit) {
        rideViewModel.setActionLoading(false)
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
                text = "HISTÓRICO DE VIAGENS",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Filtre as corridas ou busque por todas!",
                style = MaterialTheme.typography.bodySmall
            )

            RideHisotryFilter(
                isActionLoading = isActionLoading,
                onGetAll = { customerId, driverId ->
                    rideViewModel.setActionLoading(true)
                    rideViewModel.clearErrorMessage()
                    rideViewModel.fetchRidesHistory(customerId, driverId)
                }
            ) { customerId, driverId ->
                rideViewModel.setActionLoading(true)
                rideViewModel.clearErrorMessage()
                rideViewModel.fetchRidesHistory(customerId, driverId)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                if(!riderHistory.isNullOrEmpty()) {
                    LazyColumn {
                        items(riderHistory!!) { ride ->
                            RideHistoryCard(ride, modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Sem histórico para mostrar.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RideHistoryScreenPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        RideHistoryScreen(navController, hiltViewModel())
    }
}