package com.example.privateSystem.healthManagement.screens.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.components.card.HealthDataCard
import com.example.privateSystem.components.card.NewRegisterCard
import com.example.privateSystem.healthManagement.screens.HistoryDialog
import com.example.shared.BaseState
import com.example.shared.components.MyAlertDialog
import com.example.shared.components.myWeekSelector.MyWeekSelector
import com.example.shared.models.Health
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import com.example.shared.utils.getCurrentWeekRange
import com.example.shared.utils.toImageBitmap
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HealthHistoryScreen(
    healthRef: String,
) {
    val extras = MutableCreationExtras().apply {
        set(HistoryViewModel.K_COLLECTION_REF, healthRef)
    }
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory, extras = extras
    )

    val listState = viewModel.listState.observeAsState()
    val selectedDay = viewModel.selectedDate

    var currentWeek by remember {
        mutableStateOf<Pair<LocalDate, LocalDate>>(
            getCurrentWeekRange()
        )
    }

    var healthDialog by remember { mutableStateOf<Health?>(null) }
    var removeHealthDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        EventBus.sendEvent(
            UiPrivateEvent.ChangeTitles(
                "Health Data",
                "Manuel Fernandes",
                true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        //Text(healthRef)
        MyWeekSelector(
            onSelectWeek = {
                currentWeek = it
            }, onSelectDay = {
                viewModel.onChangeSelectedDate(it)
            }, currentDay = selectedDay, currentWeek = currentWeek
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cards
        PullToRefreshBox(
            isRefreshing = listState.value is BaseState.Loading,
            onRefresh = { viewModel.getHealthList(selectedDay) },
            modifier = Modifier.fillMaxSize()
        ) {

            when (listState.value) {
                is BaseState.Error -> {
                    Text("Error")
                }

                is BaseState.Success<*> -> {
                    val data = (listState.value as BaseState.Success<List<Health>>).data
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            NewRegisterCard(onClick = { healthDialog = Health.empty })
                        }

                        items(data) { item ->
                            val image by produceState<ByteArray?>(initialValue = null) {
                                value = item.getImage()
                            }

                            HealthDataCard(
                                label = item.label,
                                value = item.value,
                                unit = item.unit,
                                image = image?.toImageBitmap(),
                                onLongClick = {
                                    removeHealthDialog = item.id
                                },
                                onClick = {
                                    healthDialog = item
                                }
                            )
                        }

                    }
                }

                else -> Unit
            }

        }
    }

    when {
        healthDialog != null -> {
            HistoryDialog(
                defaultData = healthDialog!!,
                isLoading = viewModel.isLoadingForm,
                onSubmitRequest = { data ->
                    viewModel.setHealth(data.copy(date = selectedDay))
                },
                onDismissRequest = {
                    healthDialog = null
                }
            )
        }

        removeHealthDialog != null -> {
            MyAlertDialog(
                onDismissRequest = {
                    removeHealthDialog = null
                },
                onConfirmation = {
                    viewModel.removeHealth(removeHealthDialog!!)
                    removeHealthDialog = null
                },
                dialogText = "Are you sure you want to remove this health item?",
                dialogTitle = "Remove Health",
                variant = "error",
                icon = Icons.Default.HeartBroken,
                confirmText = "Remove"
            )
        }
    }


    /*if (showAddHealthRegistry) {
        HealthDataFormDialog(dismissAddHealthRegistry, {})
    }


    if (showCalendarDialog) {

    }*/
}