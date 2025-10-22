package com.example.authSystem.forms.register

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shared.models.ValidationResult
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object RegisterFormValidators {
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

    fun email(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun password(value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Password can't be null"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun birthday(value: Long?): ValidationResult {
        if (value == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "Birthday field is required"
            )
        }

        val maxDate = LocalDateTime.now().minusYears(18)
        val cDate = Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        if (cDate >= maxDate) {
            return ValidationResult(
                successful = false,
                errorMessage = "You aren't more than 18 year old"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    fun role(value: Int): ValidationResult {
        return ValidationResult(
            successful = true
        )
    }


    fun confPassword(value: String, passwordCompare: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Confirm password can't be null"
            )
        } else if (passwordCompare != value) {
            return ValidationResult(
                successful = false,
                errorMessage = "Confirm password should be the same of Password!"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

}