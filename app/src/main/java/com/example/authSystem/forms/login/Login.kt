package com.example.authSystem.forms.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authSystem.dialogs.ForgotPasswordDialog
import com.example.shared.components.MyLoading

@Composable
fun LoginForm(
    onSubmitRequest: (email: String, password: String) -> Unit,
    isLoading: Boolean = false,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showForgotPasswordDialog by remember { mutableStateOf<Boolean>(false) }
    var dismissForgotPasswordDialog = {
        showForgotPasswordDialog = false
    }

    if (isLoading) {
        MyLoading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Mail, contentDescription = null) }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Password,
                        contentDescription = null
                    )
                }
            )

            // Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Forgot your password?")
                    TextButton(onClick = {
                        showForgotPasswordDialog = true
                    }) {
                        Text("Click Here!")
                    }
                }
                ElevatedButton(onClick = {
                    onSubmitRequest(email, password)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Login")
                }
            }
        }
        if (showForgotPasswordDialog) {
            ForgotPasswordDialog(dismissForgotPasswordDialog, {})
        }
    }

}