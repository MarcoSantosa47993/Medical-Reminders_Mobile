package com.example.shared

sealed class BaseState {
    object Init : BaseState()
    object Loading : BaseState()
    data class Error(val message: String) : BaseState()
    data class Success<T>(val data: T) : BaseState()
}