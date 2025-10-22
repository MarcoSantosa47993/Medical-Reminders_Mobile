package com.example.privateSystem.medicinsManagement.states

import com.example.privateSystem.medicinsManagement.models.Medicin

data class MedicinState(
    val loading: Boolean = true,
    val data: Medicin = Medicin.empty,
    val error: String? = null
)