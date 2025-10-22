package com.example.shared.utils

import kotlinx.serialization.Serializable

sealed class Paths {
    sealed class AuthSystem : Paths() {
        @Serializable
        object Default : AuthSystem()

        @Serializable
        object LoginRegister : AuthSystem()
    }

    sealed class PrivateSystem : Paths() {
        @Serializable
        object Default : PrivateSystem()

        @Serializable
        object Home : PrivateSystem()

        @Serializable
        object Profile : PrivateSystem()

        sealed class DependentsSystem : PrivateSystem() {
            @Serializable
            object Default : DependentsSystem()

            @Serializable
            object List : DependentsSystem()

            @Serializable
            data class Details(val dependentId: String) : DependentsSystem()

            @Serializable
            data class Planning(val planningRef: String) : DependentsSystem()

            @Serializable
            data class Health(val healthRef: String) : DependentsSystem()

            sealed class RecipesSystem : DependentsSystem() {
                @Serializable
                data class List(val recipesRef: String) : RecipesSystem()

                @Serializable
                data class Details(val recipesRef: String, val recipeId: String?) : RecipesSystem()
            }

            sealed class MedicinesSystem : DependentsSystem() {
                @Serializable
                data class List(val medicinesRef: String) : MedicinesSystem()

                @Serializable
                data class Details(val medicineRef: String, val medicineId: String?) :
                    MedicinesSystem()
            }


        }
    }
}