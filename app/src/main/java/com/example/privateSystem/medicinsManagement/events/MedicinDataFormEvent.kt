package com.example.privateSystem.medicinsManagement.events

import com.example.authSystem.models.Period

sealed class MedicinDataFormEvent {
    data class NameChanged(val value: String): MedicinDataFormEvent()
    data class TypeChanged(val value: String): MedicinDataFormEvent()
    data class QuantityChanged(val value: Int?): MedicinDataFormEvent()
    data class DosePerPeriodChanged(val value: Int?): MedicinDataFormEvent()
    data class PeriodChanged(val value: Period): MedicinDataFormEvent()
    data class ObservationsChanged(val value: String): MedicinDataFormEvent()

    object Sumbit: MedicinDataFormEvent()
}