package com.example.privateSystem

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authSystem.screens.profile.ProfileScreen
import com.example.privateSystem.components.bottomBar.BottomBar
import com.example.privateSystem.components.topBar.TopBar
import com.example.privateSystem.dashboard.screens.HomeScreen
import com.example.privateSystem.dependentsManagement.dependentsManagementNavGraph
import com.example.shared.enums.MyUserRoles
import com.example.shared.models.MyUser
import com.example.shared.utils.Paths


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainPrivateSystem(
    privateNavController: NavHostController,
    title: String,
    subTitle: String?,
    backButton: Boolean,
    user: MyUser,
) {


    Scaffold(
        topBar = {
            TopBar(title, subTitle, backButton)
        },
        bottomBar = {
            BottomBar(
                privateNavController,
                user.role
            )
        },
        containerColor = Color.White

    ) { innerPadding ->
        NavHost(
            navController = privateNavController,
            startDestination = BottomBarRoutes.Home.route,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            composable<Paths.PrivateSystem.Home> {
                HomeScreen(user)
            }

            composable<Paths.PrivateSystem.Profile> {
                ProfileScreen(user)
            }

            dependentsManagementNavGraph("users/${user.id}/dependents")
        }
    }
}

sealed class BottomBarRoutes(
    val route: Paths.PrivateSystem,
    val title: String,
    val icon: ImageVector,
    val rolesAllowed: List<MyUserRoles>,
) {
    object Home : BottomBarRoutes(
        Paths.PrivateSystem.Home,
        "Home",
        Icons.Filled.Home,
        rolesAllowed = listOf(
            MyUserRoles.dependent,
            MyUserRoles.caregiver
        )
    )

    object Dependents : BottomBarRoutes(
        Paths.PrivateSystem.DependentsSystem.Default,
        "Dependents",
        Icons.Filled.Groups,
        rolesAllowed = listOf(
            MyUserRoles.caregiver
        )
    )

    object Profile : BottomBarRoutes(
        Paths.PrivateSystem.Profile,
        "Profile",
        Icons.Filled.Person,
        rolesAllowed = listOf(
            MyUserRoles.dependent,
            MyUserRoles.caregiver
        )
    )
}