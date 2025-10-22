package com.example.privateSystem.receipsManagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.privateSystem.receipsManagement.events.RecipeMedicineFormEvent
import com.example.privateSystem.receipsManagement.states.RecipeMedicineFormState
import com.example.privateSystem.receipsManagement.validators.RecipeMedicineValidators
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipeMedicineFormViewModel : ViewModel() {

    private val validator = RecipeMedicineValidators
    var state by mutableStateOf(RecipeMedicineFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: RecipeMedicineFormEvent) {
        when (event) {
            is RecipeMedicineFormEvent.MedicineNameChanged -> {
                state = state.copy(medicineName = event.value)
            }

            is RecipeMedicineFormEvent.QuantityChanged -> {
                state = state.copy(quantity = event.value)
            }

            is RecipeMedicineFormEvent.ShortDescriptionChanged -> {
                state = state.copy(shortDescription = event.value)
            }

            RecipeMedicineFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val medicineNameResult = validator.medicineName(state.medicineName)
        val shortDescriptionResult = validator.shortDescription(state.shortDescription)
        val quantityResult = validator.quantity(state.quantity)

        val hasError = listOf(
            medicineNameResult,
            shortDescriptionResult,
            quantityResult,
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                medicineNameError = medicineNameResult.errorMessage,
                shortDescriptionError = shortDescriptionResult.errorMessage,
                quantityError = quantityResult.errorMessage
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