package com.example.privateSystem.planning.validators

import com.example.shared.enums.PlanPriority
import com.example.shared.models.ValidationResult
import java.time.LocalDateTime

object PlanFormValidators {
    fun medicineId(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "MedicineId cannot be blank"
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }

    fun takeAt(value: LocalDateTime): ValidationResult {
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }

    fun priority(value: PlanPriority): ValidationResult {
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }

    fun emptyStomach(value: Boolean): ValidationResult {
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }

}