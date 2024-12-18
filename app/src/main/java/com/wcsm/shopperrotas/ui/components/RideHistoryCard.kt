package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.WatchLater
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.data.remote.dto.Driver
import com.wcsm.shopperrotas.data.remote.dto.Ride
import com.wcsm.shopperrotas.ui.theme.MoneyGreenColor
import com.wcsm.shopperrotas.ui.theme.OnPrimaryColor
import com.wcsm.shopperrotas.ui.theme.OnSurfaceColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.SurfaceColor
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.utils.distanceDoubleToKmString
import com.wcsm.shopperrotas.utils.toBRLString
import com.wcsm.shopperrotas.utils.toBrazillianDatetime

@Composable
fun RideHistoryCard(
    ride: Ride,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = SurfaceColor
        ),
        modifier = Modifier
            .width(350.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icone de pessoa",
                        tint = OnPrimaryColor
                    )
                    Text(
                        text = ride.driver.name ?: "",
                        color = OnPrimaryColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Icone de calendário",
                        tint = OnPrimaryColor
                    )
                    Text(
                        text = ride.date.toBrazillianDatetime() ?: ride.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnPrimaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = OnSurfaceColor)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = "Icone de rota",
                        tint = OnPrimaryColor
                    )
                    Text(
                        text = ride.distance.distanceDoubleToKmString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = OnPrimaryColor
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WatchLater,
                        contentDescription = "Icone de relógio",
                        tint = OnPrimaryColor
                    )
                    Text(
                        text = "Duração: ${ride.duration}",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnPrimaryColor
                    )
                }

                Text(
                    text = ride.value.toBRLString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MoneyGreenColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(thickness = 1.dp, color = OnSurfaceColor)

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonPinCircle,
                        contentDescription = "Icone de localizacao de destino",
                        tint = TertiaryColor
                    )
                    Text(
                        text = ride.origin,
                        color = TertiaryColor,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = OnPrimaryColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Icone de localizacao de destino",
                        tint = TertiaryColor
                    )
                    Text(
                        text = ride.destination,
                        color = TertiaryColor,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RideHistoryCardPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        val ride = Ride(
            id = 46,
        date = "2024-04-23T08:25:14",
        origin = "20301 Ben Oval, 847, Borerton, 04094-3164",
        destination = "8098 W Broadway Street, 662, South San Francisco, 57197",
        distance = 99.93362203989776,
        duration = "16:15",
        driver = Driver(
            id = 1,
            name = "James Bond"
        ),
        value = 899.9824824229191
        )

        RideHistoryCard(ride)
    }
}