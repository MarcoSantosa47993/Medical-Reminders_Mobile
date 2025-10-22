package com.example.privateSystem.receipsManagement.states

import java.util.Date

data class RecipeDataFormState (
    val recipeNumber: String = "",
    val recipeNumberError: String? = null,
    val emittedAt: Long? = null,
    val emittedAtError: String? = null,
    val expiresAt: Long? = null,
    val expiresAtError: String? = null,
)