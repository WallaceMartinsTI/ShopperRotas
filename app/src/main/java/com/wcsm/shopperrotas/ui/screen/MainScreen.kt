package com.wcsm.shopperrotas.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.R
import com.wcsm.shopperrotas.ui.components.LeaveAppDialog
import com.wcsm.shopperrotas.ui.components.StylizedText
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.OnSurfaceColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.PoppinsFontFamily
import com.wcsm.shopperrotas.utils.Constants
import com.wcsm.shopperrotas.viewmodel.RideViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    navController: NavController,
    rideViewModel: RideViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isActionLoading by rideViewModel.isActionLoading.collectAsStateWithLifecycle()
    var showBackHandlerDialog by remember { mutableStateOf(false) }

    BackHandler {
        showBackHandlerDialog = true
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
            Spacer(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )

            StylizedText(
                initialText = "Bem-vindo ao ",
                textToStyle = "Shopper Rotas",
                style = SpanStyle(color = PrimaryColor),
                fontFamily = PoppinsFontFamily,
                endText = "!",
                fontWeight = FontWeight.Bold,
                color = OnSurfaceColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            StylizedText(
                initialText = "Pronto para sua próxima jornada? Clique em ",
                textToStyle = "'SOLICITAR VIAGEM'",
                style = SpanStyle(color = PrimaryColor, fontWeight = FontWeight.Bold),
                fontFamily = PoppinsFontFamily,
                endText = " e descubra o caminho perfeito para você!",
                color = OnSurfaceColor,
                modifier = Modifier
                    .padding(8.dp)
                    .width(350.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = {
                    rideViewModel.setActionLoading(true)
                    navController.navigate(Screen.TravelRequest.route)
                },
                enabled = !isActionLoading
            ) {
                Text(
                    text = "SOLICITAR VIAGEM",
                    fontFamily = PoppinsFontFamily
                )
            }
        }

        if(showBackHandlerDialog) {
            LeaveAppDialog(
                onDismiss = { showBackHandlerDialog = false }
            ) {
                val activity = context as? Activity
                activity?.finish()
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}