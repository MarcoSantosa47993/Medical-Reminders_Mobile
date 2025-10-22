package com.example.authSystem.forms.profile

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

class ProfileFormViewModel : ViewModel() {
    private val validators = ProfileFormValidators

    var state by mutableStateOf(ProfileFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: ProfileFormEvent) {
        when (event) {
            is ProfileFormEvent.BirthdayChanged -> {
                state = state.copy(birthday = event.value)
            }

            is ProfileFormEvent.EmailChanged -> {
                state = state.copy(email = event.value)
            }

            is ProfileFormEvent.LocationChanged -> {
                state = state.copy(location = event.value)
            }

            is ProfileFormEvent.NameChanged -> {
                state = state.copy(name = event.value)
            }

            is ProfileFormEvent.Phone2Changed -> {
                state = state.copy(phone2 = event.value)
            }

            is ProfileFormEvent.PhoneChanged -> {
                state = state.copy(phone = event.value)
            }

            is ProfileFormEvent.RoleChanged -> {
                state = state.copy(role = event.value)
            }

            is ProfileFormEvent.ImageChanged -> {
                state = state.copy(image = event.value)
            }

            ProfileFormEvent.Submit -> {
                submitData()
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitData() {
        val nameResult = validators.name(state.name)
        val emailResult = validators.email(state.email)
        val phoneResult = validators.phone(state.phone)
        val phone2Result = validators.phone2(state.phone2)
        val locationResult = validators.location(state.location)
        val birthdayResult = validators.birthday(state.birthday)
        val roleResult = validators.role(state.role)
        val imageResult = validators.image(state.image)

        val hasError = listOf(
            nameResult,
            emailResult,
            phoneResult,
            phone2Result,
            birthdayResult,
            roleResult,
            locationResult,
            imageResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                birthdayError = birthdayResult.errorMessage,
                roleError = roleResult.errorMessage,
                phoneError = phoneResult.errorMessage,
                phone2Error = phone2Result.errorMessage,
                locationError = locationResult.errorMessage,
                imageError = imageResult.errorMessage
            )
            return
        }


        viewModelScope.launch {
            validationEventChannel.send(
                ValidationEvent.Success<ProfileFormState>(
                    data = state
                )
            )
        }
    }


}