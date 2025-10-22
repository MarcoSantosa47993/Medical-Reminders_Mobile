package com.example.authSystem.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.authSystem.screens.LoginRegisterScreen
import com.example.shared.utils.Paths

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authSystemNavGraph() {
    navigation<Paths.AuthSystem.Default>(
        startDestination = Paths.AuthSystem.LoginRegister
    ) {
        composable<Paths.AuthSystem.LoginRegister> {
            LoginRegisterScreen()
        }

    }
}

