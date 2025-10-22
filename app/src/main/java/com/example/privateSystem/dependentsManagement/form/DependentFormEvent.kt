package com.example.privateSystem.dependentsManagement.form

sealed class DependentFormEvent {
    data class NameChanged(val value: String) : DependentFormEvent()
    data class BirthdayChanged(val value: Long?) : DependentFormEvent()
    data class PhoneChanged(val value: String) : DependentFormEvent()
    data class Phone2Changed(val value: String) : DependentFormEvent()
    data class LocationChanged(val value: String) : DependentFormEvent()

    object Submit : DependentFormEvent()
}