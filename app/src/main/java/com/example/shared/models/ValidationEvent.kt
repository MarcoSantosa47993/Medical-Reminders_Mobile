package com.example.shared.models

sealed class ValidationEvent {
    data class Success<T>(val data: T) : ValidationEvent()
}