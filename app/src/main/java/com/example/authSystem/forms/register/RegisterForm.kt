package com.example.authSystem.forms.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authSystem.viewmodel.HealthDataScreenViewModel.Companion.convertMillisToDate
import com.example.shared.components.MyDateInput
import com.example.shared.components.MyLoading
import com.example.shared.components.MySelectInput
import com.example.shared.enums.MyUserRoles
import com.example.shared.models.MyUser
import com.example.shared.models.ValidationEvent
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(
    onSubmitRequest: (data: MyUser, password: String) -> Unit,
    isLoading: Boolean = false,
) {

    val viewModel = viewModel<RegisterFormViewModel>()
    val state = viewModel.state
    val context = LocalContext.current


    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val data = event.data as RegisterFormState
                val myUser = MyUser.empty.copy(
                    name = data.name,
                    email = data.email,
                    birthday = Date(data.birthday!!),
                    role = MyUserRoles.entries[data.role]
                )

                onSubmitRequest(myUser, event.data.password)
            }
        }
    }


    when {
        isLoading -> MyLoading()
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person, contentDescription = null
                        )
                    },
                    value = state.name,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.NameChanged(it)) },
                    isError = state.nameError != null,
                    supportingText = if (state.nameError != null) ({
                        Text(state.nameError)
                    }) else null

                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Mail") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Mail, contentDescription = null
                        )
                    },
                    value = state.email,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.EmailChanged(it)) },
                    isError = state.emailError != null,
                    supportingText = if (state.emailError != null) ({
                        Text(state.emailError)
                    }) else null

                )

                MyDateInput(
                    value = state.birthday?.let { convertMillisToDate(it) } ?: "",
                    label = { Text("Birthday") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DateRange, contentDescription = null
                        )
                    },
                    onValueChange = {
                        viewModel.onEvent(RegisterFormEvent.BirthdayChanged(it))
                    },
                    isError = state.birthdayError != null,
                    supportingText = if (state.birthdayError != null) ({
                        Text(state.birthdayError)
                    }) else null

                )



                MySelectInput(label = "Role", selected = state.role, onValueChange = {
                    viewModel.onEvent(RegisterFormEvent.RoleChanged(it))
                }, options = MyUserRoles.entries.associate { it.ordinal to it.name })


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Password, contentDescription = null
                        )
                    },
                    value = state.password,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.PasswordChanged(it)) },
                    isError = state.passwordError != null,
                    supportingText = if (state.passwordError != null) ({
                        Text(state.passwordError)
                    }) else null

                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirm Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Password, contentDescription = null
                        )
                    },
                    value = state.confPassword,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.ConfPasswordChanged(it)) },
                    isError = state.confPasswordError != null,
                    supportingText = if (state.confPasswordError != null) ({
                        Text(state.confPasswordError)
                    }) else null

                )

                // Actions
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    ElevatedButton(onClick = {
                        viewModel.onEvent(RegisterFormEvent.Submit)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Register")
                    }
                }


            }
        }
    }
}


