package com.example.authSystem.forms.register

sealed class RegisterFormEvent {
    data class NameChanged(val value: String) : RegisterFormEvent()
    data class EmailChanged(val value: String) : RegisterFormEvent()
    data class BirthdayChanged(val value: Long?) : RegisterFormEvent()
    data class RoleChanged(val value: Int) : RegisterFormEvent()
    data class PasswordChanged(val value: String) : RegisterFormEvent()
    data class ConfPasswordChanged(val value: String) : RegisterFormEvent()

    object Submit : RegisterFormEvent()
}