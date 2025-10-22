package com.example.authSystem.forms.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.enums.MyUserRoles
import com.example.shared.models.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class RegisterFormViewModel : ViewModel() {
    private val validator = RegisterFormValidators

    var state by mutableStateOf(RegisterFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.BirthdayChanged -> {
                state = state.copy(birthday = event.value)
            }

            is RegisterFormEvent.ConfPasswordChanged -> {
                state = state.copy(confPassword = event.value)
            }

            is RegisterFormEvent.EmailChanged -> {
                state = state.copy(email = event.value)
            }

            is RegisterFormEvent.NameChanged -> {
                state = state.copy(name = event.value)
            }

            is RegisterFormEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }

            RegisterFormEvent.Submit -> {
                submitData()
            }

            is RegisterFormEvent.RoleChanged -> {
                state = state.copy(role = event.value)
            }
        }
    }

    private fun submitData() {
        val nameResult = validator.name(state.name)
        val emailResult = validator.email(state.email)
        val passwordResult = validator.password(state.password)
        val confPasswordResult = validator.confPassword(state.confPassword, state.password)
        val birthdayResult = validator.birthday(state.birthday)
        val roleResult = validator.role(state.role)

        val hasError = listOf(
            nameResult,
            emailResult,
            passwordResult,
            confPasswordResult,
            birthdayResult,
            roleResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confPasswordError = confPasswordResult.errorMessage,
                birthdayError = birthdayResult.errorMessage,
                roleError = roleResult.errorMessage
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

data class RegisterFormState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val birthday: Long? = null,
    val birthdayError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confPassword: String = "",
    val confPasswordError: String? = null,
    val role: Int = MyUserRoles.dependent.ordinal,
    val roleError: String? = null,
)