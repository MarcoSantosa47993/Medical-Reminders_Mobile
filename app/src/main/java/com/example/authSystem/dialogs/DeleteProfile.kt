package com.example.authSystem.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.medicinsschedules.ui.theme.errorLight
import com.example.medicinsschedules.ui.theme.onSurfaceVariantLight
import com.example.medicinsschedules.ui.theme.tertiaryLight

@Composable
fun DeleteProfileDialog(
    onBackRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = { onBackRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            shape = RoundedCornerShape(34.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,


                ) {
                Column(
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = errorLight
                    )
                    Text(
                        text = "Delete Account",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(16.dp),
                        color = errorLight
                    )
                    Text(
                        text = "Are you sure you want to remove this account?",
                        fontSize = 14.sp,
                        color = onSurfaceVariantLight
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 8.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
                    ) {
                        TextButton(
                            onClick = { onBackRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Back", color = tertiaryLight)
                        }
                        Button(
                            onClick = { onConfirmation() },
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = errorLight)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

