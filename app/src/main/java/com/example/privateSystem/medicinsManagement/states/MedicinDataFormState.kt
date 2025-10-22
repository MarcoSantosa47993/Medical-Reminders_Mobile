package com.example.privateSystem.medicinsManagement.states

import com.example.authSystem.models.Period

data class MedicinDataFormState (
    val name: String = "",
    val nameError: String? = null,
    val type: String = "",
    val typeError: String? = null,
    val quantity: Int? = null,
    val quantityError: String? = null,
    val dosePerPeriod: Int? = null,
    val dosePerPeriodError: String? = null,
    val period: Period = Period.DAY,
    val observations: String = "",
    val observationsError: String? = null,
)