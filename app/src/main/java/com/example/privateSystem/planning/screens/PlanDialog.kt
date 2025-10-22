package com.example.privateSystem.planning.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.privateSystem.planning.events.PlanFormEvent
import com.example.privateSystem.planning.states.PlanFormState
import com.example.privateSystem.planning.viewmodel.PlanningFormViewModel
import com.example.shared.components.MySelectInput
import com.example.shared.components.MyTimeInput
import com.example.shared.enums.PlanPriority
import com.example.shared.enums.PlanStatus
import com.example.shared.models.Plan
import com.example.shared.models.ValidationEvent
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlanDialog(
    defaultData: Plan,
    onDismiss: () -> Unit,
    onSubmit: (Plan) -> Unit,
    isLoading: Boolean = false,
    medicines: List<Medicin>,
) {
    val viewModel = viewModel<PlanningFormViewModel>()
    val state = viewModel.state

    LaunchedEffect(Unit) {
        val zoneId = ZoneId.systemDefault()
        val takeAtInstant = defaultData.takeAt.toInstant()

        viewModel.onEvent(
            PlanFormEvent.TakeAtChanged(
                LocalDateTime.ofInstant(
                    takeAtInstant, zoneId
                )
            )
        )
        viewModel.onEvent(PlanFormEvent.PriorityChanged(defaultData.priority))
        viewModel.onEvent(PlanFormEvent.MedicineIdChanged(defaultData.medicineId))
        viewModel.onEvent(PlanFormEvent.EmptyStomachChanged(defaultData.jejum))


        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success<*> -> {
                    val data = event.data as PlanFormState

                    val p = defaultData.copy(
                        medicineId = data.medicineId,
                        takeAt = Date.from(data.takeAt.atZone(zoneId).toInstant()),
                        priority = data.priority,
                        jejum = data.emptyStomach
                    )

                    onSubmit(p)
                    viewModel.clearFormErrors()
                    onDismiss()
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = { Text(if (defaultData.id.isBlank()) "Add Plan" else "Update Plan") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MySelectInput(
                    label = "Medicine",
                    selected = state.medicineId,
                    onValueChange = { medicineId ->
                        viewModel.onEvent(
                            PlanFormEvent.MedicineIdChanged(
                                medicineId
                            )
                        )
                    },
                    options = medicines.associate { it.id to "${it.name}\t(Qty: ${it.quantity})" },
                    isError = state.medicineIdError != null,
                    supportingText = if (state.medicineIdError != null) ({
                        Text(state.medicineIdError)
                    }) else null
                )

                MyTimeInput(
                    value = state.takeAt,
                    onValueChange = {
                        viewModel.onEvent(PlanFormEvent.TakeAtChanged(it))
                    },
                    label = { Text("Take At") },
                    isError = state.takeAtError != null,
                    supportingText = if (state.takeAtError != null) ({
                        Text(state.takeAtError)
                    }) else null
                )

                MySelectInput(
                    label = "Priority",
                    selected = state.priority,
                    onValueChange = { priority ->
                        viewModel.onEvent(
                            PlanFormEvent.PriorityChanged(
                                priority
                            )
                        )
                    },
                    options = PlanPriority.entries.associate { it to it.name },
                    isError = state.priorityError != null,
                    supportingText = if (state.priorityError != null) ({
                        Text(state.priorityError)
                    }) else null
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Switch(checked = state.emptyStomach, onCheckedChange = {
                            viewModel.onEvent(PlanFormEvent.EmptyStomachChanged(it))
                        })
                    },
                    value = "Empty Stomach",
                    onValueChange = { },
                    isError = state.emptyStomachError != null,
                    supportingText = if (state.emptyStomachError != null) ({
                        Text(state.emptyStomachError)
                    }) else null

                )
            }
        },
        confirmButton = {
            Button(
                enabled = defaultData.taked == PlanStatus.PENDING,
                onClick = {
                    viewModel.onEvent(PlanFormEvent.Submit)
                }, modifier = Modifier.height(48.dp)
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss, modifier = Modifier.height(48.dp)
            ) {
                Text("Back")
            }
        })
}