package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.shopperrotas.data.dto.Driver
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.SurfaceColor
import com.wcsm.shopperrotas.ui.theme.TertiaryColor
import com.wcsm.shopperrotas.ui.theme.White06Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideHisotryFilter(
    isActionLoading: Boolean,
    onGetAll: (customerId: String, driverId: Int?) -> Unit,
    onApplyFilter: (customerId: String, driver: Driver) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var userId by rememberSaveable { mutableStateOf("") }
    var userIdError by rememberSaveable { mutableStateOf("") }
    var driversDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    var selectedDriverId by rememberSaveable { mutableIntStateOf(-1) }
    var selectedDriverName by rememberSaveable { mutableStateOf("Escolha um motorista") }
    var selectedDriverError by rememberSaveable { mutableStateOf("") }

    val driverFocusRequester = FocusRequester()

    var selectedDriver: Driver? by rememberSaveable { mutableStateOf(null) }

    var getAllButtonClicked by rememberSaveable { mutableStateOf(false) }
    var filterButtonClicked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedDriverId, selectedDriverName) {
        if(selectedDriverId != -1) {
            selectedDriver = Driver(selectedDriverId, selectedDriverName)
        }
    }

    LaunchedEffect(isActionLoading) {
        if(!isActionLoading) {
            getAllButtonClicked = false
            filterButtonClicked = false
        }
    }

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
                    contentDescription = "Icone de pessoa",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(userId.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Icone de limpar",
                        tint = White06Color,
                        modifier = Modifier.clickable { userId = "" }
                    )
                }
            },
            isError = userIdError.isNotEmpty(),
            errorMessage = userIdError,
            singleLine = true
        )

        Box {
            ExposedDropdownMenuBox(
                expanded = driversDropdownExpanded,
                onExpandedChange = {
                    driversDropdownExpanded = !driversDropdownExpanded
                }
            ) {
                CustomTextField(
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                        .focusRequester(driverFocusRequester),
                    value = selectedDriverName,
                    onValueChange = {
                        driversDropdownExpanded = !driversDropdownExpanded
                    },
                    label = {
                        Text(
                            text = "Motorista",
                            color = TertiaryColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector =
                            if (driversDropdownExpanded) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Icone de seta para cima ou para baixo",
                        )
                    },
                    isError = selectedDriverError.isNotEmpty(),
                    errorMessage = selectedDriverError,
                    readOnly = true,
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = driversDropdownExpanded,
                    onDismissRequest = { driversDropdownExpanded = false },
                    modifier = Modifier.background(SurfaceColor)
                ) {
                    val drivers = listOf(
                        Driver(
                            id = -1,
                            name = "Escolha um motorista"
                        ),
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
                                    text = driver.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = White06Color
                                )
                            },
                            leadingIcon = {
                                if(driver.id != -1) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Icone de pessoa",
                                        tint = White06Color
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.NoAccounts,
                                        contentDescription = "Icone de pessoa off",
                                        tint = White06Color
                                    )
                                }
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
                    filterButtonClicked = true
                    userIdError = ""
                    selectedDriverError = ""
                    if(userId.isEmpty()) {
                        userIdError = "Você deve informar um ID de usuário."
                        filterButtonClicked = false
                    } else if(selectedDriverId == -1) {
                        selectedDriverError = "Você deve selecionar um motorista."
                        driverFocusRequester.requestFocus()
                        filterButtonClicked = false
                    } else {
                        focusManager.clearFocus()
                        selectedDriver?.let { onApplyFilter(userId, it) }
                    }
                },
                enabled = !isActionLoading,
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = if(filterButtonClicked) "FILTRANDO..." else "APLICAR FILTRO",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    getAllButtonClicked = true
                    userIdError = ""
                    selectedDriverError = ""
                    selectedDriverId = -1
                    selectedDriverName = "Escolha um motorista"
                    if(userId.isEmpty()) {
                        userIdError = "Você deve informar um ID de usuário."
                        getAllButtonClicked = false
                    } else {
                        focusManager.clearFocus()
                        onGetAll(userId, null)
                    }
                },
                enabled = !isActionLoading,
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = if(getAllButtonClicked) "BUSCANDO..." else "LISTAS TODAS",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun TravelHisotryFilterPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        RideHisotryFilter(false, { _, _ ->}) { _, _ -> }
    }
}