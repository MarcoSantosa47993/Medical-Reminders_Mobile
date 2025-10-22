package com.example.privateSystem.healthManagement.forms.healthForm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shared.components.MyImageInput
import com.example.shared.models.Health
import com.example.shared.models.ValidationEvent
import com.example.shared.utils.toImageBitmap

@Composable
fun HealthForm(
    defaultData: Health,
    onSubmitRequest: (data: HealthFormState) -> Unit,
    viewModel: HealthFormViewModel = viewModel<HealthFormViewModel>(),
) {

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.onEvent(HealthFormEvent.LabelChanged(defaultData.label))
        viewModel.onEvent(HealthFormEvent.UnitChanged(defaultData.unit))
        viewModel.onEvent(HealthFormEvent.ValueChanged(defaultData.value.toString()))
        viewModel.onEvent(HealthFormEvent.ImageChanged(defaultData.getImage()?.toImageBitmap()))

        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val data = event.data as HealthFormState
                onSubmitRequest(data)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MyImageInput(
            value = state.image,
            onChanged = {
                viewModel.onEvent(HealthFormEvent.ImageChanged(it))
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Label") },
            value = state.label,
            onValueChange = { viewModel.onEvent(HealthFormEvent.LabelChanged(it)) },
            isError = state.labelError != null,
            supportingText = if (state.labelError != null) ({
                Text(state.labelError)
            }) else null
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Value") },
            value = state.value,
            onValueChange = { viewModel.onEvent(HealthFormEvent.ValueChanged(it)) },
            isError = state.valueError != null,
            supportingText = if (state.valueError != null) ({
                Text(state.valueError)
            }) else null
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Unit") },
            value = state.unit,
            onValueChange = { viewModel.onEvent(HealthFormEvent.UnitChanged(it)) },
            isError = state.unitError != null,
            supportingText = if (state.unitError != null) ({
                Text(state.unitError)
            }) else null
        )
    }
}