package com.example.privateSystem.receipsManagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.privateSystem.receipsManagement.events.RecipeDataFormEvent
import com.example.privateSystem.receipsManagement.states.RecipeDataFormState
import com.example.privateSystem.receipsManagement.validators.RecipeDataFormValidators
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecipeDataFormViewModel: ViewModel() {
    private val validator = RecipeDataFormValidators

    var state by mutableStateOf(RecipeDataFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RecipeDataFormEvent) {
        when (event){
            is RecipeDataFormEvent.EmittedAtChanged -> {
                state = state.copy(emittedAt = event.value)
            }
            is RecipeDataFormEvent.ExpiresAtChanged -> {
                state = state.copy(expiresAt = event.value)
            }
            is RecipeDataFormEvent.RecipeNumberChanged -> {
                state = state.copy(recipeNumber = event.value)
            }
            RecipeDataFormEvent.Sumbit -> {
                submitData()
            }
        }
    }

    fun clearErrors() {
        state = state.copy(
            recipeNumberError = null,
            expiresAtError = null,
            emittedAtError = null
        )
    }

    private fun submitData() {
        val recipeNumberResult = validator.recipeNumber(state.recipeNumber)
        val emittedAtResult = validator.emittedAt(state.emittedAt)
        val expiresAtResult = validator.expiresAt(state.expiresAt)

        val hasError = listOf(
            recipeNumberResult,
            emittedAtResult,
            expiresAtResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                recipeNumberError = recipeNumberResult.errorMessage,
                expiresAtError = expiresAtResult.errorMessage,
                emittedAtError = emittedAtResult.errorMessage
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