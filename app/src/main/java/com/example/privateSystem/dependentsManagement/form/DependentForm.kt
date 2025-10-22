package com.example.privateSystem.dependentsManagement.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authSystem.viewmodel.HealthDataScreenViewModel.Companion.convertMillisToDate
import com.example.shared.components.MyDateInput
import com.example.shared.components.MyLoading
import com.example.shared.models.Dependent
import com.example.shared.models.ValidationEvent
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DependentForm(
    defaultData: Dependent,
    onSubmitRequest: (data: Dependent) -> Unit,
    isLoading: Boolean = false,
) {
    val viewModel = viewModel<DependentFormViewModel>()
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.onEvent(DependentFormEvent.NameChanged(defaultData.name))
        viewModel.onEvent(DependentFormEvent.BirthdayChanged(defaultData.birthday.time))
        viewModel.onEvent(DependentFormEvent.PhoneChanged(defaultData.phone))
        viewModel.onEvent(DependentFormEvent.Phone2Changed(defaultData.phone2))
        viewModel.onEvent(DependentFormEvent.LocationChanged(defaultData.location))

        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val data = event.data as DependentFormState
                val dependent = defaultData.copy(
                    name = data.name,
                    birthday = Date(data.birthday!!),
                    location = data.location,
                    phone = data.phone,
                    phone2 = data.phone2
                )
                onSubmitRequest(dependent)
            }
        }
    }

    if (isLoading) {
        MyLoading()
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                value = state.name,
                onValueChange = { viewModel.onEvent(DependentFormEvent.NameChanged(it)) },
                isError = state.nameError != null,
                supportingText = if (state.nameError != null) ({
                    Text(state.nameError)
                }) else null

            )

            MyDateInput(
                value = state.birthday?.let { convertMillisToDate(it) } ?: "",
                label = { Text("Birthday") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null
                    )
                },
                onValueChange = {
                    viewModel.onEvent(DependentFormEvent.BirthdayChanged(it))
                },
                isError = state.birthdayError != null,
                supportingText = if (state.birthdayError != null) ({
                    Text(state.birthdayError)
                }) else null

            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Location") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null
                    )
                },
                value = state.location,
                onValueChange = { viewModel.onEvent(DependentFormEvent.LocationChanged(it)) },
                isError = state.locationError != null,
                supportingText = if (state.locationError != null) ({
                    Text(state.locationError)
                }) else null

            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null
                    )
                },
                value = state.phone,
                onValueChange = { viewModel.onEvent(DependentFormEvent.PhoneChanged(it)) },
                isError = state.phoneError != null,
                supportingText = if (state.phoneError != null) ({
                    Text(state.phoneError)
                }) else null

            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone (alternative)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null
                    )
                },
                value = state.phone2,
                onValueChange = { viewModel.onEvent(DependentFormEvent.Phone2Changed(it)) },
                isError = state.phone2Error != null,
                supportingText = if (state.phone2Error != null) ({
                    Text(state.phone2Error)
                }) else null

            )


            ElevatedButton(
                onClick = {
                    viewModel.onEvent(DependentFormEvent.Submit)
                },
                modifier = Modifier.fillMaxWidth(),

                ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Update",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text("Update")
            }
        }
    }
}