package com.example.medicinsschedules

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.authSystem.AuthSystem
import com.example.medicinsschedules.ui.theme.AppTheme
import com.example.privateSystem.MainPrivateSystem
import com.example.shared.events.UiAuthEvent
import com.example.shared.utils.AppWrite
import com.example.shared.utils.EventBus
import com.example.shared.utils.Paths
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()

        AppWrite.init(applicationContext)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
            val navController = rememberNavController()
            val privateNavController = rememberNavController()
            val st = privateNavController.saveState()
            val state = authViewModel.authState.observeAsState()

            var title by remember { mutableStateOf<String>("title") }
            var subTitle by remember { mutableStateOf<String?>("subtitle") }
            var backButton by remember { mutableStateOf<Boolean>(false) }


            LaunchedEffect(key1 = lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    EventBus.events.collect { event ->
                        when (event) {
                            UiEvent.NavigateToAuthSystem -> {
                                navController.navigate(Paths.AuthSystem.Default)
                            }

                            UiEvent.NavigateToPrivateSystem -> {
                                navController.navigate(Paths.PrivateSystem.Default)
                            }

                            UiEvent.RefetchSession -> {
                                authViewModel.checkAuthStatus()
                            }

                            is UiEvent.Toast -> {
                                Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is UiPrivateEvent.ChangeTitles -> {
                                title = event.title
                                subTitle = event.subtitle
                                backButton = event.backButton
                            }

                            is UiPrivateEvent.SetUser -> {
                                authViewModel.setUser(event.myUser)
                            }

                            is UiPrivateEvent.NavigateBack -> {
                                privateNavController.popBackStack()
                            }

                            is UiPrivateEvent.Navigate -> {
                                privateNavController.navigate(event.path)
                            }

                            is UiPrivateEvent.Logout -> {
                                authViewModel.signout()
                            }

                            is UiAuthEvent.Login -> {
                                authViewModel.login(event.email, event.password)
                            }

                            is UiAuthEvent.Register -> {
                                authViewModel.signup(event.myUser, event.password)
                            }
                        }
                    }
                }
            }

            LaunchedEffect(state.value) {
                when (state.value) {
                    is AuthState.Error -> {
                        EventBus.sendEvent(UiEvent.Toast((state.value as AuthState.Error).message))
                    }

                    else -> Unit
                }
            }

            AppTheme {
                when {
                    state.value is AuthState.Init -> {
                        SplashScreen()
                    }

                    state.value is AuthState.Authenticated -> {
                        MainPrivateSystem(
                            privateNavController,
                            title,
                            subTitle,
                            backButton,
                            (state.value as AuthState.Authenticated).myUser
                        )
                    }

                    state.value is AuthState.Unauthenticated -> {
                        AuthSystem(navController)
                    }
                }


            }
        }
    }
}
