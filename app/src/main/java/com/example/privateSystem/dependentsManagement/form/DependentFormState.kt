package com.example.privateSystem.dependentsManagement.form

data class DependentFormState(
    val name: String = "",
    val nameError: String? = null,
    val birthday: Long? = null,
    val birthdayError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val phone2: String = "",
    val phone2Error: String? = null,
    val location: String = "",
    val locationError: String? = null,
)