package com.example.privateSystem.medicinsManagement.validators

import com.example.shared.models.ValidationResult

object MedicinDataFormValidators {
    fun name(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name can't be null"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    fun type(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Type can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun quantity(value: Int?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Quantity can't be null"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    fun dosePerPeriod(value: Int?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Dose Per Period can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun observations(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Observations can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}