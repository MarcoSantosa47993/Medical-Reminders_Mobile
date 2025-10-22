package com.example.privateSystem.receipsManagement.states

data class RecipeMedicineFormState (
    val medicineName: String = "",
    val medicineNameError: String? = null,
    val shortDescription: String = "",
    val shortDescriptionError: String? = null,
    val quantity: String = "",
    val quantityError: String? = null,
)