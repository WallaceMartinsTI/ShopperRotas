package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.data.remote.dto.RideOption
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@Composable
fun DriversAvailable(
    drivers: List<RideOption>?,
    isActionLoading: Boolean,
    onConfirmDriver: (driver: RideOption) -> Unit,
) {
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(!drivers.isNullOrEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(drivers) { driver ->
                    DriverCard(
                        driver = driver,
                        isActionLoading = isActionLoading,
                        onConfirmDriver = { chosenDriver ->
                            onConfirmDriver(chosenDriver)
                        },
                        isExpanded = expandedStates[driver.id] ?: false,
                        onExpandChange = { expanded ->
                            expandedStates[driver.id]  = expanded
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhum motorista disponível no momento.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(280.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DriversAvailablePreview() {
    ShopperRotasTheme(dynamicColor = false) {
        //DriversAvailable(emptyList()) {}
    }
}