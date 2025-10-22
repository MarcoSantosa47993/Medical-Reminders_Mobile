package com.example.authSystem.screens.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.authSystem.forms.profile.ProfileForm
import com.example.authSystem.screens.profile.component.ProfileToolbar
import com.example.shared.models.MyUser
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(myUser: MyUser) {
    val scope = rememberCoroutineScope()

    var shareProfileDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        EventBus.sendEvent(
            UiPrivateEvent.ChangeTitles(
                title = "Profile",
                subtitle = myUser.name,
                false
            )
        )
    }


    Content(
        defaultData = myUser,
        onShareProfileRequest = {
            shareProfileDialog = true
        },
        onSubmitRequest = {
            scope.launch {
                EventBus.sendEvent(UiPrivateEvent.SetUser(it))
            }
        },
        onLogoutRequest = {
            scope.launch {
                EventBus.sendEvent(
                    UiPrivateEvent.Logout
                )
            }
        }
    )

    // dialogs
    when {
        shareProfileDialog -> DialogShareProfile(
            pinCode = myUser.pinCode.toInt(),
            onDismissRequest = {
                shareProfileDialog = false
            },

            )
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Content(
    defaultData: MyUser,
    onShareProfileRequest: () -> Unit,
    onSubmitRequest: (MyUser) -> Unit,
    onLogoutRequest: () -> Unit,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        ProfileToolbar(
            onShareProfileRequest = onShareProfileRequest,
            onChangePasswordRequest = {}
        )

        ProfileForm(
            defaultData = defaultData,
            onSubmitRequest = onSubmitRequest,
            onLogoutRequest = onLogoutRequest
        )
    }


}