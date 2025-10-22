package com.example.authSystem.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medicinsschedules.R
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView

@Composable
fun DialogShareProfile(pinCode: Int, onDismissRequest: () -> Unit) {

    AlertDialog(
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        iconContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(text = "Share Profile")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                QrCodeView(
                    data = pinCode.toString(),
                    colors = QrCodeColors(
                        background = MaterialTheme.colorScheme.surfaceContainerHigh,
                        foreground = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(75.dp)
                            .width(75.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text("Pin: ", fontWeight = FontWeight.SemiBold)
                    Text(pinCode.toString())
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Back")
            }
        }
    )

    /*Dialog(
        onDismissRequest = onDismiss,


        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(top = 24.dp, start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    "Share Profile",
                    style = MaterialTheme.typography.headlineSmall

                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Image(
                        bitmap = qrCodeGenerated.asImageBitmap(),
                        contentDescription = "dwdwd",
                        modifier = Modifier
                            .height(160.dp),

                        )

                    Text(
                        text = "Pin: 1234",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black

                    )
                }


                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text("Back")
                }

            }


        }
    }*/
}


