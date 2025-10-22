package com.example.authSystem

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authSystem.screens.LoginRegisterScreen
import com.example.shared.utils.Paths

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthSystem(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Paths.AuthSystem.LoginRegister
    ) {
        composable<Paths.AuthSystem.LoginRegister> {
            LoginRegisterScreen()
        }
    }
}