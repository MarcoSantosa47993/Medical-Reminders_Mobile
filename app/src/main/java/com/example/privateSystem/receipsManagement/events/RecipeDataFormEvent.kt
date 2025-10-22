package com.example.privateSystem.receipsManagement.events

sealed class RecipeDataFormEvent {
    data class RecipeNumberChanged(val value: String): RecipeDataFormEvent()
    data class EmittedAtChanged(val value: Long?): RecipeDataFormEvent()
    data class ExpiresAtChanged(val value: Long?): RecipeDataFormEvent()

    object Sumbit: RecipeDataFormEvent()
}