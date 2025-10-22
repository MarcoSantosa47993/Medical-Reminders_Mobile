package com.example.privateSystem.receipsManagement.models

data class RecipeMedicineDialogState(
    val isOpen: Boolean,
    val data: RecipeMedicine?,
    val readOnly: Boolean = false
)