package com.wcsm.shopperrotas.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.shopperrotas.R
import com.wcsm.shopperrotas.ui.model.Screen
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.OnSurfaceColor
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.utils.StylizedText

@Composable
fun MainScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
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
                endText = "!",
                fontWeight = FontWeight.Bold,
                color = OnSurfaceColor
            )

            StylizedText(
                initialText = "Pronto para sua próxima jornada? Clique em ",
                textToStyle = "'SOLICITAR VIAGEM'",
                style = SpanStyle(color = PrimaryColor, fontWeight = FontWeight.Bold),
                endText = "e descubra o caminho perfeito para você!",
                color = OnSurfaceColor,
                modifier = Modifier.padding(8.dp).width(350.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.TravelRequest.route)
                }
            ) {
                Text("SOLICITAR VIAGEM")
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