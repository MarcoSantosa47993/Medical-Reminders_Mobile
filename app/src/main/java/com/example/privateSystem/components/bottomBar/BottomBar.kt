package com.example.privateSystem.components.bottomBar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.privateSystem.BottomBarRoutes
import com.example.shared.enums.MyUserRoles

@Composable
fun BottomBar(navController: NavHostController, userRole: MyUserRoles) {
    val navBarItems = listOf(
        BottomBarRoutes.Home,
        BottomBarRoutes.Dependents,
        BottomBarRoutes.Profile
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination
        navBarItems.filter { it.rolesAllowed.contains(userRole) }.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it ->
                    it.hasRoute(screen.route::class)
                } == true,
                label = { Text(screen.title) },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = null)
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}