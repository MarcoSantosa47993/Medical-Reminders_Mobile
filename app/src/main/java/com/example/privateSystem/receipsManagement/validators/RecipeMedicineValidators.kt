package com.example.privateSystem.receipsManagement.validators

import com.example.shared.models.ValidationResult

object RecipeMedicineValidators {
    fun medicineName(value: String): ValidationResult {

        if (value.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Medicine Name is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }


    fun shortDescription(value: String): ValidationResult {
        if (value.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Short Description is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun quantity(value: String): ValidationResult {
        try {
            val v = value.toInt()
            if (v <= 0) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Quantity can't be less than 1"
                )
            }

            return ValidationResult(
                successful = true
            )
        }catch (_: NumberFormatException){
            return ValidationResult(
                successful = false,
                errorMessage = "Quantity is not a valid number"
            )
        }
    }
}