package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import com.wcsm.shopperrotas.ui.theme.ErrorColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@Composable
fun TravelRequestForm(
    isSubmitButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onSubmit: (customerId: String, origin: String, destination: String) -> Unit,
) {

    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031") }
    var destination by remember { mutableStateOf("Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200") }

/*

    var customerId by remember { mutableStateOf("Qualquer") }
    var origin by remember { mutableStateOf("Qualquer") }
    var destination by remember { mutableStateOf("Qualquer") }
*/

    Column(
        modifier = Modifier
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
                Text("Usuário")
            },
            placeholder = {
                Text("Informe o ID do usuário.")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        CustomTextField(
            value = origin,
            onValueChange = {
                origin = it
            },
            label = {
                Text("Origem")
            },
            placeholder = {
                Text("Informe o endereço de origem.")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        CustomTextField(
            value = destination,
            onValueChange = {
                destination = it
            },
            label = {
                Text("Destino")
            },
            placeholder = {
                Text("Informe o endereço de destino.")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        if(!errorMessage.isNullOrEmpty()) {
            Text(
                text = "Erro: $errorMessage",
                color = ErrorColor,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = {
                onSubmit(customerId, origin, destination)
            },
            enabled = isSubmitButtonEnabled
        ) {
            Text("ESTIMAR VALOR")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelRequestFormPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        TravelRequestForm(true) { _, _, _ -> }
    }
}