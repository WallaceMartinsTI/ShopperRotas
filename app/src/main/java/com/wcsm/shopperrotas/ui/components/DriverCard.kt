package com.wcsm.shopperrotas.ui.components

import androidx.compose.runtime.Composable
import com.wcsm.shopperrotas.data.model.RideOption

@Composable
fun DriverCard(
    driver: RideOption,
    isExpanded: Boolean,
    onConfirmDriver: (driver: RideOption) -> Unit,
    onExpandChange: (Boolean) -> Unit
) {
    if(isExpanded) {
        DriverCardExpanded(
            driver = driver,
            onChooseDriver = { onConfirmDriver(driver) }
        ) { expanded ->
            onExpandChange(expanded)
        }
    } else {
        DriverCardRetracted(
            driver = driver
        ) { onExpandChange(true) }
    }
}