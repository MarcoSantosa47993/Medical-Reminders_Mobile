package com.example.privateSystem.healthManagement.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.healthManagement.forms.healthForm.HealthForm
import com.example.privateSystem.healthManagement.forms.healthForm.HealthFormEvent
import com.example.privateSystem.healthManagement.forms.healthForm.HealthFormViewModel
import com.example.shared.models.Health
import com.example.shared.utils.AppWrite
import kotlinx.coroutines.launch

@Composable
fun HistoryDialog(
    defaultData: Health,
    onSubmitRequest: (Health) -> Unit,
    onDismissRequest: () -> Unit,
    isLoading: Boolean,
) {
    val scope = rememberCoroutineScope()

    val viewModel = viewModel<HealthFormViewModel>()

    var loading by remember { mutableStateOf<Boolean>(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(16.dp),
        ) {
            if (isLoading || loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier.padding(
                    vertical = 10.dp,
                    horizontal = 20.dp,
                )
            ) {
                Text(
                    text = "Health Registry",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    fontSize = 24.sp,
                )

                HealthForm(
                    defaultData = defaultData,
                    onSubmitRequest = { data ->
                        scope.launch {
                            loading = true
                            var imgId = defaultData.image

                            if (data.image != null) {
                                if (imgId != null) {
                                    AppWrite.removeFile(imgId)
                                }

                                val file = AppWrite.uploadFile(data.image)
                                imgId = file?.id
                            }

                            if (data.image == null && imgId != null) {
                                AppWrite.removeFile(imgId)
                                imgId = null
                            }

                            val health = defaultData.copy(
                                label = data.label,
                                unit = data.unit,
                                value = data.value.toDouble(),
                                image = imgId
                            )
                            loading = false
                            onSubmitRequest(health)
                            onDismissRequest()
                        }
                    },
                    viewModel = viewModel
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            if (!isLoading || !loading) {
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = !isLoading || !loading,
                    ) {
                        Text("Back", color = Color(0xFF006A6A))
                    }
                    Button(
                        onClick = {
                            if (!isLoading || !loading) {
                                viewModel.onEvent(HealthFormEvent.Submit)
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = !isLoading || !loading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006A6A))
                    ) {
                        Text("Submit")
                    }


                }

            }
        }
    }
}