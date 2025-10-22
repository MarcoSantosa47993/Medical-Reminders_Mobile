package com.example.privateSystem.dependentsManagement.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DependentFormViewModel : ViewModel() {
    private val validators = DependentFormValidators

    var state by mutableStateOf(DependentFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: DependentFormEvent) {
        when (event) {
            is DependentFormEvent.BirthdayChanged -> {
                state = state.copy(birthday = event.value)
            }

            is DependentFormEvent.LocationChanged -> {
                state = state.copy(location = event.value)
            }

            is DependentFormEvent.NameChanged -> {
                state = state.copy(name = event.value)
            }

            is DependentFormEvent.Phone2Changed -> {
                state = state.copy(phone2 = event.value)
            }

            is DependentFormEvent.PhoneChanged -> {
                state = state.copy(phone = event.value)
            }


            DependentFormEvent.Submit -> {
                submitData()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitData() {
        val nameResult = validators.name(state.name)
        val phoneResult = validators.phone(state.phone)
        val phone2Result = validators.phone2(state.phone2)
        val locationResult = validators.location(state.location)
        val birthdayResult = validators.birthday(state.birthday)

        val hasError = listOf(
            nameResult,
            phoneResult,
            phone2Result,
            birthdayResult,
            locationResult,
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                birthdayError = birthdayResult.errorMessage,
                phoneError = phoneResult.errorMessage,
                phone2Error = phone2Result.errorMessage,
                locationError = locationResult.errorMessage,
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