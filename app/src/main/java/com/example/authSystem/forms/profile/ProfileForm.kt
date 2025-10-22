package com.example.authSystem.forms.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authSystem.viewmodel.HealthDataScreenViewModel.Companion.convertMillisToDate
import com.example.medicinsschedules.ui.theme.errorLight
import com.example.shared.components.MyDateInput
import com.example.shared.components.MyImageInput
import com.example.shared.components.MySelectInput
import com.example.shared.enums.MyUserRoles
import com.example.shared.models.MyUser
import com.example.shared.models.ValidationEvent
import com.example.shared.utils.AppWrite
import com.example.shared.utils.toImageBitmap
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileForm(
    defaultData: MyUser,
    onSubmitRequest: (data: MyUser) -> Unit,
    onLogoutRequest: () -> Unit,
) {

    val viewModel = viewModel<ProfileFormViewModel>()
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.onEvent(ProfileFormEvent.NameChanged(defaultData.name))
        viewModel.onEvent(ProfileFormEvent.EmailChanged(defaultData.email))
        viewModel.onEvent(ProfileFormEvent.RoleChanged(defaultData.role.ordinal))
        viewModel.onEvent(ProfileFormEvent.BirthdayChanged(defaultData.birthday.time))
        viewModel.onEvent(ProfileFormEvent.PhoneChanged(defaultData.phone))
        viewModel.onEvent(ProfileFormEvent.Phone2Changed(defaultData.phone2))
        viewModel.onEvent(ProfileFormEvent.LocationChanged(defaultData.location))
        viewModel.onEvent(ProfileFormEvent.ImageChanged(defaultData.getImage()?.toImageBitmap()))

        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val data = event.data as ProfileFormState

                var imgId = defaultData.image

                if (data.image != null) {
                    if (imgId != null) {
                        AppWrite.removeFile(imgId)
                    }

                    val file = AppWrite.uploadFile(data.image)
                    imgId = file?.id
                }

                if (data.image == null && imgId != null) {
                    AppWrite.removeFile(imgId)
                    imgId = null
                }

                val myUser = defaultData.copy(
                    name = data.name,
                    email = data.email,
                    birthday = Date(data.birthday!!),
                    role = MyUserRoles.entries[data.role],
                    location = data.location,
                    phone = data.phone,
                    phone2 = data.phone2,
                    image = imgId
                )

                onSubmitRequest(myUser)
            }
        }
    }

    Column(

        verticalArrangement = Arrangement.spacedBy(20.dp)

    ) {
        MyImageInput(
            value = state.image,
            onChanged = {
                viewModel.onEvent(ProfileFormEvent.ImageChanged(it))
            }
        )


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
            onValueChange = { viewModel.onEvent(ProfileFormEvent.NameChanged(it)) },
            isError = state.nameError != null,
            supportingText = if (state.nameError != null) ({
                Text(state.nameError)
            }) else null

        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mail") },
            leadingIcon = { Icon(imageVector = Icons.Filled.Mail, contentDescription = null) },
            value = state.email,
            onValueChange = { viewModel.onEvent(ProfileFormEvent.EmailChanged(it)) },
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
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null
                )
            },
            onValueChange = {
                viewModel.onEvent(ProfileFormEvent.BirthdayChanged(it))
            },
            isError = state.birthdayError != null,
            supportingText = if (state.birthdayError != null) ({
                Text(state.birthdayError)
            }) else null

        )

        MySelectInput(
            label = "Role",
            selected = state.role,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null
                )
            },
            onValueChange = {
                viewModel.onEvent(ProfileFormEvent.RoleChanged(it))
            },
            options = MyUserRoles.entries.associate { it.ordinal to it.name }
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
            onValueChange = { viewModel.onEvent(ProfileFormEvent.LocationChanged(it)) },
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
            onValueChange = { viewModel.onEvent(ProfileFormEvent.PhoneChanged(it)) },
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
            onValueChange = { viewModel.onEvent(ProfileFormEvent.Phone2Changed(it)) },
            isError = state.phone2Error != null,
            supportingText = if (state.phone2Error != null) ({
                Text(state.phone2Error)
            }) else null

        )

        ElevatedButton(
            onClick = {
                viewModel.onEvent(ProfileFormEvent.Submit)
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

        TextButton(
            modifier = Modifier.fillMaxSize(),

            onClick = {
                onLogoutRequest()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.width(8.dp))
            Text("Logout", color = errorLight, fontWeight = FontWeight.Bold)
        }


        TextButton(
            modifier = Modifier.fillMaxSize(),

            onClick = {
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = errorLight
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Remove", color = errorLight, fontWeight = FontWeight.Bold)
        }

        /*if (showDeleteProfile) {
            DeleteProfileDialog(onBackRequest = { showDeleteProfile = false }) { }
        }*/
    }


}
