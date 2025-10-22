package com.example.privateSystem.healthManagement.forms.healthForm

import android.graphics.Bitmap
import com.example.shared.models.ValidationResult

object HealthFormValidators {
    fun label(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Label can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun value(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Value can't be null"
            )
        }

        if (value.toDoubleOrNull() == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid number"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun unit(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Unit can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun image(value: Bitmap?): ValidationResult {

        return ValidationResult(
            successful = true
        )
    }
}