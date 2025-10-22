package com.example.privateSystem.receipsManagement.events

sealed class RecipeMedicineFormEvent {
    data class MedicineNameChanged(val value: String) : RecipeMedicineFormEvent()
    data class ShortDescriptionChanged(val value: String) : RecipeMedicineFormEvent()
    data class QuantityChanged(val value: String) : RecipeMedicineFormEvent()

    object Submit : RecipeMedicineFormEvent()
}