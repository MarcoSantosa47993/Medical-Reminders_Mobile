package com.example.privateSystem.medicinsManagement.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authSystem.dialogs.DeleteMedicinDialog
import com.example.authSystem.models.Period
import com.example.medicinsschedules.ui.theme.errorLight
import com.example.privateSystem.medicinsManagement.events.MedicinDataFormEvent
import com.example.privateSystem.medicinsManagement.states.MedicinDataFormState
import com.example.privateSystem.medicinsManagement.viewmodel.MedicinDataFormViewmodel
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.privateSystem.receipsManagement.events.RecipeDataFormEvent
import com.example.shared.components.MyLoading
import com.example.shared.models.ValidationEvent
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinDetailForm(
    defaultData: Medicin,
    onSubmitRequest: (Medicin) -> Unit,
    isLoading: Boolean = false
) {
    val viewModel = viewModel<MedicinDataFormViewmodel>()
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(MedicinDataFormEvent.NameChanged(defaultData.name))
        viewModel.onEvent(MedicinDataFormEvent.TypeChanged(defaultData.type))
        viewModel.onEvent(MedicinDataFormEvent.QuantityChanged(defaultData.quantity))
        viewModel.onEvent(MedicinDataFormEvent.DosePerPeriodChanged(defaultData.dosePerPeriod))
        viewModel.onEvent(MedicinDataFormEvent.PeriodChanged(defaultData.period))
        viewModel.onEvent(MedicinDataFormEvent.ObservationsChanged(defaultData.observations))

        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success<*> -> {
                    val data = event.data as MedicinDataFormState
                    val d = defaultData.copy(
                        name = data.name,
                        type = data.type,
                        quantity = data.quantity!!,
                        dosePerPeriod = data.dosePerPeriod!!,
                        observations = data.observations
                    )
                    onSubmitRequest(d)
                    viewModel.clearErrors()
                }
            }
        }
    }

    var showRemoveMedicinDialog by remember { mutableStateOf<Boolean>(false) }
    var dismissAddMedicinDialog = {
        showRemoveMedicinDialog = false
    }

    var expanded by remember { mutableStateOf(false) }
    val options = Period.entries.toList()

    if(isLoading){
        MyLoading()
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                label = { Text("Name") },
                onValueChange = {
                    viewModel.onEvent(MedicinDataFormEvent.NameChanged(it))
                },
                isError = state.nameError != null,
                supportingText = if (state.nameError != null) ({
                    Text(state.nameError)
                }) else null
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.type,
                label = { Text("Type") },
                onValueChange = {
                    viewModel.onEvent(MedicinDataFormEvent.TypeChanged(it))
                },
                isError = state.typeError != null,
                supportingText = if (state.typeError != null) ({
                    Text(state.typeError)
                }) else null
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.quantity}",
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    viewModel.onEvent(MedicinDataFormEvent.QuantityChanged(it.toIntOrNull() ?: 0))
                },
                isError = state.quantityError != null,
                supportingText = if (state.quantityError != null) ({
                    Text(state.quantityError)
                }) else null
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.dosePerPeriod}",
                label = { Text("Dose Per Period") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    viewModel.onEvent(
                        MedicinDataFormEvent.DosePerPeriodChanged(
                            it.toIntOrNull() ?: 0
                        )
                    )
                },
                isError = state.dosePerPeriodError != null,
                supportingText = if (state.dosePerPeriodError != null) ({
                    Text(state.dosePerPeriodError)
                }) else null
            )

            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable),
                        value = state.period.displayName,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Period") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { periodOption ->
                            DropdownMenuItem(
                                text = { Text(periodOption.toString()) },
                                onClick = {
                                    viewModel.onEvent(
                                        MedicinDataFormEvent.PeriodChanged(
                                            periodOption
                                        )
                                    )
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().height(118.dp),
                value = state.observations,
                label = { Text("Observations") },
                onValueChange = {
                    viewModel.onEvent(MedicinDataFormEvent.ObservationsChanged(it))
                },
                isError = state.observationsError != null,
                supportingText = if (state.observationsError != null) ({
                    Text(state.observationsError)
                }) else null
            )

            ElevatedButton(onClick = {
                viewModel.onEvent(MedicinDataFormEvent.Sumbit)
            }, modifier = Modifier.fillMaxWidth()) {
                Text(if (defaultData.id == "") "Add" else "Update")
            }
        }
    }
}


