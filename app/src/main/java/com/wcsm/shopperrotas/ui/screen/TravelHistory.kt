package com.wcsm.shopperrotas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.shopperrotas.ui.components.TravelCard
import com.wcsm.shopperrotas.ui.components.TravelHisotryFilter
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.viewmodel.TravelViewModel

@Composable
fun TravelHistory(
    travelViewModel: TravelViewModel = viewModel()
) {
    val riderHistory by travelViewModel.ridesHistory.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HISTÓRICO DE VIAGENS",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            Text("Filtre as corridas ou busque por todas!")

            TravelHisotryFilter(
                onGetAll = { customerId ->
                    travelViewModel.fetchRidesHistory(customerId, null)
                }
            ) {}

            Spacer(modifier = Modifier.height(20.dp))

            if(!riderHistory.isNullOrEmpty()) {
                LazyColumn {
                    items(riderHistory!!) { ride ->
                        TravelCard(ride, modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            } else {
                Text("Sem histórico para mostrar.")
            }

        }
    }
}

@Preview
@Composable
fun TravelHistoryPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        TravelHistory()
    }
}