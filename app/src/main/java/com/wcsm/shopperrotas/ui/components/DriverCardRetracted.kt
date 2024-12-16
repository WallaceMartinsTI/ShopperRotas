package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.shopperrotas.R
import com.wcsm.shopperrotas.data.remote.dto.Review
import com.wcsm.shopperrotas.data.remote.dto.RideOption
import com.wcsm.shopperrotas.ui.theme.MoneyGreenColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.SurfaceColor
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.utils.toBRLString

@Composable
fun DriverCardRetracted(
    driver: RideOption,
    onExpandClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = SurfaceColor
        ),
        modifier = Modifier.width(350.dp).clickable {
            onExpandClick()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.perfil),
                    contentDescription = "Imagem de perfil do motorista",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = driver.name,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = driver.vehicle,
                        fontSize = 14.sp,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(200.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seta para baixo",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = driver.value.toBRLString(),
                    color = MoneyGreenColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    Text(
                        text = driver.review.rating.toString(),
                        color = TertiaryColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Icone de estrela",
                        tint = TertiaryColor,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DriverCardPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val driver = RideOption(
            id = 1,
            name = "Homer Simpson",
            description = "Olá! Sou o Homer, seu motorista camarada! Relaxe e aproveite o passeio, com direito a rosquinhas e boas risadas (e talvez alguns desvios).",
            vehicle = "Plymouth Valiant 1973 rosa e enferrujado",
            review = Review(
                rating = 2.0,
                ""
            ),
            value = 50.05
        )
        DriverCardRetracted(driver = driver) {}
    }
}