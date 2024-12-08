package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.ui.screen.TravelRequest
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@Composable
fun TravelRequestForm(
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit
) {
    var userId by rememberSaveable { mutableStateOf("") }
    var originAddress by rememberSaveable { mutableStateOf("") }
    var destinationAddress by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(400.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTextField(
            value = userId,
            onValueChange = {
                userId = it
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
            value = originAddress,
            onValueChange = {
                originAddress = it
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
            value = destinationAddress,
            onValueChange = {
                destinationAddress = it
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

        Button(
            onClick = {
                onSubmit()
            }
        ) {
            Text("ESTIMAR VALOR")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TravelRequestFormPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        TravelRequestForm() {}
    }
}