package com.wcsm.shopperrotas.ui.components

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.data.model.Driver
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHisotryFilter(
    onGetAll: (customerId: String, driverId: Int?) -> Unit,
    onApplyFilter: () -> Unit
) {
    var userId by rememberSaveable { mutableStateOf("") }
    var userIdError by rememberSaveable { mutableStateOf("") }
    var driversDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    var selectedDriverId by rememberSaveable { mutableIntStateOf(-1) }
    var selectedDriverName by rememberSaveable { mutableStateOf("Escolha um motorista") }

    Column(
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
            isError = userIdError.isNotEmpty(),
            errorMessage = userIdError
        )

        Box {
            ExposedDropdownMenuBox(
                expanded = driversDropdownExpanded,
                onExpandedChange = {
                    driversDropdownExpanded = !driversDropdownExpanded
                }
            ) {
                CustomTextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedDriverName,
                    onValueChange = {
                        driversDropdownExpanded = !driversDropdownExpanded
                    },
                    label = {
                        Text("Motorista")
                    },
                    readOnly = true,
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = driversDropdownExpanded,
                    onDismissRequest = { driversDropdownExpanded = false }
                ) {
                    val drivers = listOf(
                        Driver(
                            id = 1,
                            name = "Homer Simpson"
                        ),
                        Driver(
                            id = 2,
                            name = "Dominic Toretto"
                        ),
                        Driver(
                            id = 3,
                            name = "James Bond"
                        )
                    )

                    drivers.forEach { driver ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${driver.id} - ${driver.name}"
                                )
                            },
                            onClick = {
                                selectedDriverId = driver.id
                                selectedDriverName = driver.name
                                driversDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Row {
            Button(
                onClick = {
                    onApplyFilter()
                }
            ) {
                Text("APLICAR FILTRO")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    userIdError = ""
                    if(userId.isEmpty()) {
                        userIdError = "Você deve informar um ID de usuário."
                    } else {
                        if(selectedDriverId != -1) {
                            onGetAll(userId, selectedDriverId)
                        } else {
                            onGetAll(userId, null)
                        }
                    }
                }
            ) {
                Text("LISTAS TODAS")
            }
        }
    }
}

@Preview
@Composable
fun TravelHisotryFilterPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        TravelHisotryFilter({ _, _ ->}) {}
    }
}