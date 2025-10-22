package com.example.shared.models

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)