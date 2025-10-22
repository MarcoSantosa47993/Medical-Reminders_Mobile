package com.example.authSystem.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.authSystem.forms.login.LoginForm
import com.example.authSystem.forms.register.RegisterForm
import com.example.medicinsschedules.R
import com.example.shared.events.UiAuthEvent
import com.example.shared.models.MyUser
import com.example.shared.utils.EventBus
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginRegisterScreen() {
    val scope = rememberCoroutineScope()

    Content(
        onLoginRequest = { email, password ->
            scope.launch {
                EventBus.sendEvent(UiAuthEvent.Login(email, password))
            }
        },
        onRegisterRequest = { myUser, password ->
            scope.launch {
                EventBus.sendEvent(UiAuthEvent.Register(myUser, password))
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Content(
    onRegisterRequest: (MyUser, String) -> Unit,
    onLoginRequest: (email: String, password: String) -> Unit,
) {
    val titles = listOf("Login", "Register")
    var state by remember { mutableIntStateOf(0) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // IMAGE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 75.dp, 0.dp, 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            // TABS
            TabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = { Text(title) })
                }
            }

            when (state) {
                1 -> RegisterForm(
                    onSubmitRequest = onRegisterRequest
                )

                else -> LoginForm(onSubmitRequest = onLoginRequest)
            }
        }
    }
}