package com.wcsm.shopperrotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wcsm.shopperrotas.ui.theme.BackgroundColor
import com.wcsm.shopperrotas.ui.theme.PoppinsFontFamily
import com.wcsm.shopperrotas.ui.theme.PrimaryColor
import com.wcsm.shopperrotas.ui.theme.ShopperRotasTheme
import com.wcsm.shopperrotas.ui.theme.SurfaceColor

@Composable
fun LeaveAppDialog(
    onDismiss: () -> Unit,
    onExitApp: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(SurfaceColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "Shopper Rotas",
                color = PrimaryColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Text(
                text = "Deseja sair do app?",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onDismiss()
                    onExitApp()
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = "SAIR DO APP",
                    fontFamily = PoppinsFontFamily
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = "CANCELAR",
                    fontFamily = PoppinsFontFamily
                )
            }
        }
    }
}

@Preview
@Composable
private fun LogoutDialogPreview() {
    ShopperRotasTheme(dynamicColor = false) {
        LeaveAppDialog(
            onExitApp = {},
            onDismiss = {}
        )
    }
}