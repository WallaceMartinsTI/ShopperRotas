package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.ErrorColor
import com.wcsm.shopperrotas.ui.theme.PoppinsFontFamily
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.ui.theme.White06Color

@Composable
fun RideRequestForm(
    isActionLoading: Boolean,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onSubmit: (customerId: String, origin: String, destination: String) -> Unit,
) {

    /*  NOTE
    *   Below are values to avoid the need to write
    *   in the input (used to speed up testing)
    */

    // RETURN WITH 3 AVAILABLE DRIVERS

    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031") }
    var destination by remember { mutableStateOf("Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200") }


    // RETURN WITH 2 AVAILABLE DRIVERS
    /*
    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000") }
    var destination by remember { mutableStateOf("Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200") }
    */

    // RETURN WITH 1 AVAILABLE DRIVER
    /*
    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001") }
    var destination by remember { mutableStateOf("Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200") }
    */

    // RETURN WITH NO AVAILABLE DRIVERS
    /*
    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Qualquer1") }
    var destination by remember { mutableStateOf("Qualquer2") }
    */
/*

    var customerId by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
*/

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .width(400.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTextField(
            value = customerId,
            onValueChange = {
                customerId = it
            },
            label = {
                Text(
                    text = "Usuário",
                    color = TertiaryColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            placeholder = {
                Text("Informe o ID do usuário.")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icone de perfil",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(customerId.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Icone de apagar",
                        tint = White06Color,
                        modifier = Modifier.clickable { customerId = "" }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
        )

        CustomTextField(
            value = origin,
            onValueChange = {
                origin = it
            },
            label = {
                Text(
                    text = "Origem",
                    color = TertiaryColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            placeholder = {
                Text("Informe o endereço de origem.")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PersonPinCircle,
                    contentDescription = "Icone de localizacao da pessoa",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(origin.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Icone de apagar",
                        tint = White06Color,
                        modifier = Modifier.clickable { origin = "" }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            maxLines = 3
        )

        CustomTextField(
            value = destination,
            onValueChange = {
                destination = it
            },
            label = {
                Text(
                    text = "Destino",
                    color = TertiaryColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            placeholder = {
                Text("Informe o endereço de destino.")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Icone de localizacao da pessoa",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(destination.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Icone de apagar",
                        tint = White06Color,
                        modifier = Modifier.clickable { destination = "" }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        if(!errorMessage.isNullOrEmpty()) {
            Text(
                text = "Erro: $errorMessage",
                color = ErrorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = {
                onSubmit(customerId, origin, destination)
            },
            enabled = !isActionLoading
        ) {
            Text(
                text = if(isActionLoading) "CALCULANDO..." else "ESTIMAR VALOR",
                fontFamily = PoppinsFontFamily
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelRequestFormPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        RideRequestForm(false) { _, _, _ -> }
    }
}