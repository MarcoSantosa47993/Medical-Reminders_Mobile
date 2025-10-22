package com.example.privateSystem.dashboard.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.components.alertItem.AlertItem
import com.example.privateSystem.components.stepper.StepperItem
import com.example.privateSystem.components.stepper.stepPlan
import com.example.privateSystem.dashboard.viewmodel.DashboardViewModel
import com.example.shared.BaseState
import com.example.shared.components.MySelectInput
import com.example.shared.components.myWeekSelector.MyWeekSelector
import com.example.shared.enums.MyUserRoles
import com.example.shared.enums.PlanStatus
import com.example.shared.models.Dependent
import com.example.shared.models.MyUser
import com.example.shared.models.Plan
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import com.example.shared.utils.getCurrentWeekRange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
private val formatter = DateTimeFormatter.ofPattern("HH:mm")

@RequiresApi(Build.VERSION_CODES.O)
private val zoneId = ZoneId.systemDefault()


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: MyUser,
) {

    val extras = MutableCreationExtras().apply {
        set(DashboardViewModel.K_COLLECTION_REF, "users/${user.id}")
        set(DashboardViewModel.K_USER_ID, user.id)
        set(DashboardViewModel.K_USER_ROLE, user.role)
    }
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory, extras = extras
    )

    val listDependentsState = viewModel.listDependents.observeAsState()
    val selectedDependent = viewModel.selectedDependent

    val medicines = viewModel.medicines.observeAsState()

    val listAlertsState = viewModel.listAlerts.observeAsState()
    val listPlansState = viewModel.listPlans.observeAsState()


    var currentWeek by remember {
        mutableStateOf<Pair<LocalDate, LocalDate>>(
            getCurrentWeekRange()
        )
    }


    LaunchedEffect(Unit) {
        EventBus.sendEvent(
            UiPrivateEvent.ChangeTitles(
                "Home", "Welcome ${user.name}", false
            )
        )
    }

    LaunchedEffect(user.role, user.id) {
        viewModel.load(user.role, user.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (user.role == MyUserRoles.caregiver) {
            MySelectInput(
                label = "Dependent", selected = selectedDependent, onValueChange = {
                    viewModel.onChangeSelectedDependent(it)
                }, leadingIcon = if (listDependentsState.value is BaseState.Error) ({
                    IconButton(onClick = { viewModel.getDependents() }) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                    }
                }) else null, options = when (listDependentsState.value) {
                    is BaseState.Init -> mapOf(
                        "" to "Loading..."
                    )

                    is BaseState.Loading -> mapOf(
                        "" to "Loading..."
                    )

                    is BaseState.Error -> mapOf(
                        "" to "Error"
                    )

                    is BaseState.Success<*> -> (listDependentsState.value as BaseState.Success<List<Dependent>>).data.associate { it.id to it.name }

                    else -> emptyMap()
                }
            )
        }


        if (selectedDependent.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Select Dependent to start ...")
            }
        } else {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Alerts",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (listAlertsState.value) {
                                is BaseState.Error -> {
                                    Text("Error: " + (listAlertsState.value as BaseState.Error).message)
                                    TextButton(onClick = { viewModel.getPlanAlerts() }) { Text("Refresh Alerts") }
                                }

                                is BaseState.Success<*> -> {
                                    val data =
                                        (listAlertsState.value as BaseState.Success<*>).data as List<Plan>

                                    if (data.isEmpty()) {
                                        Text("No alerts founded")
                                        TextButton(onClick = { viewModel.getPlanAlerts() }) { Text("Refresh Alerts") }
                                    }

                                    data.map {
                                        AlertItem(
                                            medicines.value?.get(it.medicineId) ?: "NA",
                                            "Did you take your medicin at ${
                                                formatter.format(
                                                    LocalDateTime.ofInstant(
                                                        it.takeAt.toInstant(),
                                                        zoneId
                                                    )
                                                )
                                            }?",
                                            onTake = {
                                                viewModel.changePlanStatus(it, PlanStatus.TAKED)
                                            },
                                            onNotTake = {
                                                viewModel.changePlanStatus(it, PlanStatus.NOT_TAKED)
                                            }
                                        )
                                    }
                                }

                                else -> LinearProgressIndicator()
                            }
                        }
                    }
                }

                item {
                    Column(
                        Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),

                        ) {
                        Text(
                            "Schedules",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(10.dp))
                        MyWeekSelector(
                            onSelectWeek = {
                                currentWeek = it
                            },
                            onSelectDay = {
                                viewModel.onChangeSelectedDate(it)
                            },
                            currentDay = viewModel.selectedDate,
                            currentWeek = currentWeek
                        )
                        Spacer(Modifier.height(10.dp))
                        Column(
                            Modifier
                                .padding(top = 30.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (listPlansState.value) {
                                is BaseState.Error -> {
                                    Text("Error: " + (listPlansState.value as BaseState.Error).message)
                                    TextButton(onClick = { viewModel.getDayPlan(viewModel.selectedDate) }) {
                                        Text(
                                            "Refresh Plans"
                                        )
                                    }
                                }

                                is BaseState.Success<*> -> {
                                    val data =
                                        (listPlansState.value as BaseState.Success<*>).data as List<Plan>

                                    if (data.isEmpty()) {
                                        Text("No plans founded")
                                        TextButton(onClick = { viewModel.getDayPlan(viewModel.selectedDate) }) {
                                            Text(
                                                "Refresh Plans"
                                            )
                                        }
                                    } else {
                                        StepperItem {


                                            data.forEachIndexed { index, plan ->
                                                stepPlan(
                                                    plan = plan,
                                                    index = index,
                                                    castToMedicineName = { medicineId ->
                                                        medicines.value?.get(medicineId) ?: "NA"
                                                    })
                                            }
                                        }
                                    }

                                }

                                else -> LinearProgressIndicator()
                            }
                        }


                    }
                }
            }
        }


    }
}
