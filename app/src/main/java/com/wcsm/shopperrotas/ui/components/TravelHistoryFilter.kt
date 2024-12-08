package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHisotryFilter(
    onApplyFilter: () -> Unit
) {
    var userId by rememberSaveable { mutableStateOf("") }
    var driversDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedDriver by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTextField(
            value = userId,
            onValueChange = {},
            label = {
                Text("Usuário")
            },
            placeholder = {
                Text("Informe o ID do usuário.")
            }
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
                    value = selectedDriver,
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
                        "Motorista 1",
                        "Motorista 2",
                        "Motorista 3"
                    )

                    drivers.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it
                                )
                            },
                            onClick = {
                                selectedDriver = it
                                driversDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                onApplyFilter()
            }
        ) {
            Text("APLICAR FILTRO")
        }
    }
}

@Preview
@Composable
fun TravelHisotryFilterPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        TravelHisotryFilter() {}
    }
}