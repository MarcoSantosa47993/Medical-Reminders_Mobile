package com.example.privateSystem.receipsManagement.validators

import com.example.shared.models.ValidationResult

object RecipeDataFormValidators {
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

    fun recipeNumber(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Recipe number can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun emittedAt(value: Long?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Emitted At field is required"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    fun expiresAt(value: Long?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Expires At field is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}