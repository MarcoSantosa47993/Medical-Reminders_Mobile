package com.example.privateSystem.medicinsManagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.privateSystem.medicinsManagement.states.MedicinDataFormState
import com.example.privateSystem.medicinsManagement.validators.MedicinDataFormValidators
import com.example.privateSystem.medicinsManagement.events.MedicinDataFormEvent
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MedicinDataFormViewmodel: ViewModel() {
    private val validator = MedicinDataFormValidators

    var state by mutableStateOf(MedicinDataFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: MedicinDataFormEvent) {
        when (event){
            is MedicinDataFormEvent.NameChanged -> {
                state = state.copy(name = event.value)
            }
            is MedicinDataFormEvent.TypeChanged -> {
                state = state.copy(type = event.value)
            }
            is MedicinDataFormEvent.QuantityChanged -> {
                state = state.copy(quantity = event.value)
            }
            is MedicinDataFormEvent.DosePerPeriodChanged -> {
                state = state.copy(dosePerPeriod = event.value)
            }
            is MedicinDataFormEvent.PeriodChanged -> {
                state = state.copy(period = event.value)
            }
            is MedicinDataFormEvent.ObservationsChanged -> {
                state = state.copy(observations = event.value)
            }
            MedicinDataFormEvent.Sumbit -> {
                submitData()
            }
        }
    }

    fun clearErrors() {
        state = state.copy(
            nameError = null,
            typeError = null,
            quantityError = null,
            dosePerPeriodError = null,
            observationsError = null,
        )
    }

    private fun submitData() {
        val nameResult = validator.name(state.name)
        val typeResult = validator.type(state.type)
        val quantityResult = validator.quantity(state.quantity)
        val dosePerPeriodResult = validator.dosePerPeriod(state.dosePerPeriod)
        val observationsResult = validator.observations(state.observations)

        val hasError = listOf(
            nameResult,
            typeResult,
            quantityResult,
            dosePerPeriodResult,
            observationsResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                typeError = typeResult.errorMessage,
                quantityError = quantityResult.errorMessage,
                dosePerPeriodError = dosePerPeriodResult.errorMessage,
                observationsError = observationsResult.errorMessage,
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success(
                data = state
            ))
        }
    }
}