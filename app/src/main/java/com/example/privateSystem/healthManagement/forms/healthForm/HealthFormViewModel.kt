package com.example.privateSystem.healthManagement.forms.healthForm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class HealthFormViewModel : ViewModel() {
    private val validators = HealthFormValidators

    var state by mutableStateOf(HealthFormState())


    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: HealthFormEvent) {
        when (event) {
            is HealthFormEvent.ImageChanged -> {
                state = state.copy(image = event.value)
            }

            is HealthFormEvent.LabelChanged -> {
                state = state.copy(label = event.value)
            }

            is HealthFormEvent.UnitChanged -> {
                state = state.copy(unit = event.value)
            }

            is HealthFormEvent.ValueChanged -> {
                state = state.copy(value = event.value)
            }

            HealthFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val labelResult = validators.label(state.label)
        val unitResult = validators.unit(state.unit)
        val valueResult = validators.value(state.value)
        val imageResult = validators.image(state.image)

        val hasError = listOf(
            labelResult,
            unitResult,
            valueResult,
            imageResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                labelError = labelResult.errorMessage,
                unitError = unitResult.errorMessage,
                valueError = valueResult.errorMessage,
                imageError = imageResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(
                ValidationEvent.Success(
                    data = state
                )
            )
        }
    }
}