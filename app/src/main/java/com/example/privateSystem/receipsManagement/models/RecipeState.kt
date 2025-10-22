package com.example.privateSystem.receipsManagement.models

data class RecipeState (
    val loading: Boolean = true,
    val data: Recipe = Recipe.empty,
    val error: String? = null
)