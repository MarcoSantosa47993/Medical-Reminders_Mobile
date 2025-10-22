package com.example.privateSystem.medicinsManagement.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medicinsschedules.ui.theme.primaryLight
import com.example.privateSystem.medicinsManagement.forms.MedicinDetailForm
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.privateSystem.medicinsManagement.viewmodel.MedicinsDetailsScreenViewModel
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.livedata.observeAsState
import com.example.privateSystem.medicinsManagement.viewmodel.MedicinsListScreenViewModel
import com.example.shared.BaseState
import com.example.shared.components.MyDeleteButton
import com.example.shared.utils.UiEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicinDetailsScreen(
    medicinesRef: String,
    medicinId : String?,
) {
    if(medicinId == null){
        val extras = MutableCreationExtras().apply {
            set(MedicinsListScreenViewModel.K_COLLECTION_REF, medicinesRef)
        }
        val viewModel: MedicinsListScreenViewModel = viewModel(
            factory = MedicinsListScreenViewModel.Factory,
            extras = extras
        )

        val listState = viewModel.listState.observeAsState()

        LaunchedEffect(Unit) {
            val name = viewModel.getDependentName(medicinesRef) ?: "Dependent Name"
            EventBus.sendEvent(
                UiPrivateEvent.ChangeTitles(
                    "New Medicine", name, true
                )
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Medicine Data",
                    modifier = Modifier.weight(1f),
                    color = primaryLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn {
                item {
                    MedicinDetailForm(
                        defaultData = Medicin.empty,
                        onSubmitRequest = {
                            viewModel.addMedicin(it)
                        },
                        isLoading = listState.value is BaseState.Loading
                    )
                }
            }
        }
    }

    else{
        val extras = MutableCreationExtras().apply {
            set(MedicinsDetailsScreenViewModel.K_COLLECTION_REF, medicinesRef)
        }

        val viewModel: MedicinsDetailsScreenViewModel = viewModel(
            factory = MedicinsDetailsScreenViewModel.Factory,
            extras = extras
        )

        val state = viewModel.state.observeAsState()

        LaunchedEffect(state.value) {
            val name = viewModel.getDependentName(medicinesRef) ?: "Dependent Name"
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
                    val medicin = (state.value as BaseState.Success<*>).data as Medicin
                    EventBus.sendEvent(
                        UiPrivateEvent.ChangeTitles(
                            medicin.name, name, true
                        )
                    )
                }
                else -> Unit
            }
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Medicine Data",
                    modifier = Modifier.weight(1f),
                    color = primaryLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn {
                if (state.value is BaseState.Success<*>) {
                    item {
                        MedicinDetailForm(
                            defaultData = (state.value as BaseState.Success<*>).data as Medicin,
                            onSubmitRequest = {
                                viewModel.setMedicin(it)
                            },
                            isLoading = state.value is BaseState.Loading
                        )
                    }

                    item{
                        Box(modifier = Modifier.height(10.dp))
                    }

                    item {
                        MyDeleteButton(
                            onDelete = {
                                viewModel.removeMedicin()
                            },
                            dialogTitle = "Remove medicine",
                            dialogText = "Are you sure you want to remove this medicine?",
                            icon = Icons.Default.Delete
                        )
                    }

                    item{
                        Box(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

