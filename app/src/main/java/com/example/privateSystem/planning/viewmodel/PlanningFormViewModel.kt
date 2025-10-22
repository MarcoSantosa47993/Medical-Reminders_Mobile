package com.example.privateSystem.planning.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.privateSystem.planning.events.PlanFormEvent
import com.example.privateSystem.planning.states.PlanFormState
import com.example.privateSystem.planning.validators.PlanFormValidators
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class PlanningFormViewModel : ViewModel() {
    private val validator = PlanFormValidators

    var state by mutableStateOf(PlanFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: PlanFormEvent) {
        when (event) {
            is PlanFormEvent.EmptyStomachChanged -> {
                state = state.copy(emptyStomach = event.value)
            }

            is PlanFormEvent.MedicineIdChanged -> {
                state = state.copy(medicineId = event.value)
            }

            is PlanFormEvent.PriorityChanged -> {
                state = state.copy(priority = event.value)
            }

            is PlanFormEvent.TakeAtChanged -> {
                state = state.copy(takeAt = event.value)
            }

            PlanFormEvent.Submit -> {
                submitData()
            }
        }
    }

    fun clearFormErrors() {
        state = state.copy(
            emptyStomachError = null,
            priorityError = null,
            takeAtError = null,
            medicineIdError = null
        )
    }

    private fun submitData() {
        val emptyStomachResult = validator.emptyStomach(state.emptyStomach)
        val medicineIdResult = validator.medicineId(state.medicineId)
        val priorityResult = validator.priority(state.priority)
        val takeAtResult = validator.takeAt(state.takeAt)

        val hasError = listOf(
            emptyStomachResult,
            medicineIdResult,
            priorityResult,
            takeAtResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                emptyStomachError = emptyStomachResult.errorMessage,
                medicineIdError = medicineIdResult.errorMessage,
                priorityError = priorityResult.errorMessage,
                takeAtError = takeAtResult.errorMessage
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