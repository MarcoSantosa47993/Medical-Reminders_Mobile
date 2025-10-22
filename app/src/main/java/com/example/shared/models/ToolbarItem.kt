package com.example.shared.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.shared.utils.Paths

sealed class ToolbarItem(
    val title: String,
    val icon: ImageVector,
    val route: Paths.PrivateSystem? = null,
) {

    data class Health(val dependentRef: String) : ToolbarItem(
        "Health History",
        Icons.Filled.MonitorHeart,
        Paths.PrivateSystem.DependentsSystem.Health("$dependentRef/health")
    )

    data class Receips(val dependentRef: String) : ToolbarItem(
        "Medic Receips",
        Icons.Filled.FilePresent,
        Paths.PrivateSystem.DependentsSystem.RecipesSystem.List("$dependentRef/receipts")
    )

    data class Planning(val dependentRef: String) : ToolbarItem(
        "Planning Medicines",
        Icons.Filled.CalendarMonth,
        Paths.PrivateSystem.DependentsSystem.Planning("$dependentRef/planning")
    )

    data class Medicins(val dependentRef: String) : ToolbarItem(
        "Medicines",
        Icons.Filled.Medication,
        Paths.PrivateSystem.DependentsSystem.MedicinesSystem.List("$dependentRef/medicins")
    )

    object ChangePassword : ToolbarItem(
        "Change Password",
        Icons.Filled.Password
    )

    object ShareProfile : ToolbarItem(
        "Share Profile",
        Icons.Filled.QrCode
    )
}