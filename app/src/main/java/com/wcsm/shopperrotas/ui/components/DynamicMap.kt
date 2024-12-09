package com.wcsm.shopperrotas.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun DynamicMap(
    originLatLng: LatLng,
    destinationLatLng: LatLng
) {
    val origimMarkerState = rememberMarkerState(position = originLatLng)
    val destinationMarkerState = rememberMarkerState(position = destinationLatLng)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(originLatLng, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = origimMarkerState,
            title = "Origem",
            snippet = "Marcador de Origem"
        )

        Marker(
            state = destinationMarkerState,
            title = "Destino",
            snippet = "Marcador de Destino"
        )
    }
}