package com.example.privateSystem.dependentsManagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.privateSystem.dependentsManagement.screens.details.DependentDetailsScreen
import com.example.privateSystem.dependentsManagement.screens.list.DependentsListScreen
import com.example.privateSystem.healthManagement.screens.history.HealthHistoryScreen
import com.example.privateSystem.medicinsManagement.screens.MedicinDetailsScreen
import com.example.privateSystem.medicinsManagement.screens.MedicinsListScreen
import com.example.privateSystem.planning.screens.PlanningScreen
import com.example.privateSystem.receipsManagement.screens.ListReceipsScreen
import com.example.privateSystem.receipsManagement.screens.ReceipDetailsScreen
import com.example.shared.utils.Paths

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun NavGraphBuilder.dependentsManagementNavGraph(dependentsRef: String) {
    navigation<Paths.PrivateSystem.DependentsSystem.Default>(
        startDestination = Paths.PrivateSystem.DependentsSystem.List,
    ) {
        composable<Paths.PrivateSystem.DependentsSystem.List> {
            DependentsListScreen(dependentsRef = dependentsRef)
        }

        composable<Paths.PrivateSystem.DependentsSystem.Details> {
            val model: Paths.PrivateSystem.DependentsSystem.Details = it.toRoute()
            DependentDetailsScreen("$dependentsRef/${model.dependentId}")
        }

        composable<Paths.PrivateSystem.DependentsSystem.Planning> {
            val model: Paths.PrivateSystem.DependentsSystem.Planning = it.toRoute()
            PlanningScreen(model.planningRef)
        }

        composable<Paths.PrivateSystem.DependentsSystem.RecipesSystem.List> {
            val model: Paths.PrivateSystem.DependentsSystem.RecipesSystem.List = it.toRoute()
            ListReceipsScreen(model.recipesRef)
        }

        composable<Paths.PrivateSystem.DependentsSystem.RecipesSystem.Details> {
            val model: Paths.PrivateSystem.DependentsSystem.RecipesSystem.Details = it.toRoute()
            ReceipDetailsScreen(model.recipesRef,  model.recipeId ?: ""   )
        }

        composable<Paths.PrivateSystem.DependentsSystem.MedicinesSystem.List> {
            val model: Paths.PrivateSystem.DependentsSystem.MedicinesSystem.List = it.toRoute()
            MedicinsListScreen(model.medicinesRef)
        }

        composable<Paths.PrivateSystem.DependentsSystem.MedicinesSystem.Details> {
            val model: Paths.PrivateSystem.DependentsSystem.MedicinesSystem.Details = it.toRoute()
            if (model.medicineId == null) {
                MedicinDetailsScreen(model.medicineRef, model.medicineId)
            } else {
                MedicinDetailsScreen("${model.medicineRef}/${model.medicineId}", model.medicineId)
            }
        }

        composable<Paths.PrivateSystem.DependentsSystem.Health> {
            val model: Paths.PrivateSystem.DependentsSystem.Health = it.toRoute()
            HealthHistoryScreen(model.healthRef)
        }
    }
}