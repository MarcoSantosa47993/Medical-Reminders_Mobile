package com.example.privateSystem.planning.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medicinsschedules.ui.theme.outlineLight
import com.example.medicinsschedules.ui.theme.primaryLight
import com.example.privateSystem.planning.components.PlanRow
import com.example.privateSystem.planning.viewmodel.PlanningScreenViewModel
import com.example.shared.BaseState
import com.example.shared.components.MyAlertDialog
import com.example.shared.components.myWeekSelector.MyWeekSelector
import com.example.shared.models.Plan
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import com.example.shared.utils.getCurrentWeekRange
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun PlanningScreen(
    planningRef: String,
) {

    val extras = MutableCreationExtras().apply {
        set(PlanningScreenViewModel.K_COLLECTION_REF, planningRef)
    }
    val viewModel: PlanningScreenViewModel = viewModel(
        factory = PlanningScreenViewModel.Factory, extras = extras
    )

    val listState = viewModel.listState.observeAsState()
    val medicines = viewModel.medicines
    val selectedDay = viewModel.selectedDate


    var currentWeek by remember {
        mutableStateOf<Pair<LocalDate, LocalDate>>(
            getCurrentWeekRange()
        )
    }

    fun castToMedicineName(medicineId: String): String {
        return medicines.find { it.id == medicineId }?.name ?: "NA"
    }

    var planDialog by remember { mutableStateOf<Plan?>(null) }
    var removePlanDialog by remember { mutableStateOf<Pair<String, String>?>(null) } //Pair<PlanId, MedicineId>

    LaunchedEffect(Unit) {
        EventBus.sendEvent(
            UiPrivateEvent.ChangeTitles(
                "Planning Medicines", "Manuel Fernandes", true
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //Text(planningRef)
        MyWeekSelector(
            onSelectWeek = {
                currentWeek = it
            }, onSelectDay = {
                viewModel.onChangeSelectedDate(it)
            }, currentDay = selectedDay, currentWeek = currentWeek
        )

        Spacer(Modifier.height(16.dp))

        PullToRefreshBox(
            isRefreshing = listState.value is BaseState.Loading,
            onRefresh = { viewModel.getPlans(selectedDay) },
            modifier = Modifier.fillMaxSize()
        ) {
            when (listState.value) {
                is BaseState.Error -> {
                    Text("Error")
                }

                is BaseState.Success<*> -> SuccessContent(
                    plans = (listState.value as BaseState.Success<List<Plan>>).data,
                    castToMedicineName = { medicineId ->
                        castToMedicineName(medicineId)
                    },
                    onClickPlan = { plan ->
                        planDialog = plan
                    },
                    onLongClickPlan = { plan ->
                        removePlanDialog = Pair(plan.id, plan.medicineId)
                    }
                )

                else -> Unit
            }

        }

        when {
            planDialog != null -> {
                PlanDialog(
                    defaultData = planDialog!!,
                    onDismiss = { planDialog = null },
                    onSubmit = {
                        viewModel.setPlan(it)
                    },
                    isLoading = viewModel.isLoadingForm,
                    medicines = medicines,
                )
            }

            removePlanDialog != null -> {
                MyAlertDialog(
                    onDismissRequest = {
                        removePlanDialog = null
                    },
                    onConfirmation = {
                        viewModel.removePlan(removePlanDialog!!.first, removePlanDialog!!.second)
                        removePlanDialog = null
                    },
                    dialogText = "Are you sure you want to remove this plan?",
                    dialogTitle = "Remove Plan",
                    variant = "error",
                    icon = Icons.Default.Schedule,
                    confirmText = "Remove"
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SuccessContent(
    plans: List<Plan>,
    castToMedicineName:
        (String) -> String,
    onClickPlan: (Plan) -> Unit,
    onLongClickPlan: (Plan) -> Unit,
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, outlineLight),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(1.dp, outlineLight, RectangleShape)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Medicine", Modifier.weight(6f / 12f), fontWeight = Bold)
                Text(
                    "Time",
                    Modifier.weight(3f / 12f),
                    fontWeight = Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Taken",
                    Modifier.weight(3f / 12f),
                    fontWeight = Bold,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                /*items(planningList) { med ->
                    MedicineItem(med) { updated ->
                        planningList = planningList.map {
                            if (it.name == updated.name) updated else it
                        }
                    }
                }*/
                if (plans.isEmpty()) {
                    item {
                        Text(
                            "No plans founded",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }


                items(plans) { plan ->
                    PlanRow(
                        plan = plan,
                        castToMedicineName = castToMedicineName,
                        onClick = { onClickPlan(plan) },
                        onLongClick = { onLongClickPlan(plan) }
                    )
                }
            }

            OutlinedButton(
                onClick = { onClickPlan(Plan.empty) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RectangleShape
            ) {
                Text("Add Medicine", fontSize = 16.sp, color = primaryLight)
            }
        }
    }
}


