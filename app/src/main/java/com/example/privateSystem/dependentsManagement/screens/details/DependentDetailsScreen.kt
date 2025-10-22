package com.example.privateSystem.dependentsManagement.screens.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.dependentsManagement.form.DependentForm
import com.example.privateSystem.dependentsManagement.screens.details.components.DependentToolbar
import com.example.shared.BaseState
import com.example.shared.components.MyDeleteButton
import com.example.shared.models.Dependent
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DependentDetailsScreen(
    dependentRef: String,
) {
    val extras = MutableCreationExtras().apply {
        set(DependentsDetailsScreenViewModel.K_COLLECTION_REF, dependentRef)
    }
    val viewModel: DependentsDetailsScreenViewModel = viewModel(
        factory = DependentsDetailsScreenViewModel.Factory, extras = extras
    )

    val state = viewModel.state.observeAsState()

    LaunchedEffect(state.value) {
        when (state.value) {
            is BaseState.Error -> {
                EventBus.sendEvent(
                    UiEvent.Toast((state.value as BaseState.Error).message)
                )
                EventBus.sendEvent(
                    UiPrivateEvent.NavigateBack
                )
            }

            is BaseState.Success<*> -> {
                val dependent = (state.value as BaseState.Success<*>).data as Dependent
                EventBus.sendEvent(
                    UiPrivateEvent.ChangeTitles(
                        "Dependent", dependent.name, true
                    )
                )
            }

            else -> Unit
        }
    }

    Column {
        LazyColumn {
            item {
                DependentToolbar(dependentRef)
            }

            if (state.value is BaseState.Success<*>) {
                item {
                    DependentForm(
                        defaultData = (state.value as BaseState.Success<*>).data as Dependent,
                        onSubmitRequest = {
                            viewModel.setDependent(it)
                        },
                        isLoading = state.value is BaseState.Loading
                    )
                }

            }
        }

        if (state.value is BaseState.Success<*>) {
            MyDeleteButton(
                onDelete = {
                    viewModel.removeDependent()
                },
                dialogTitle = "Remove dependent",
                dialogText = "Are you sure you want to remove this dependent?",
                icon = Icons.Rounded.Person
            )
        }
    }


}