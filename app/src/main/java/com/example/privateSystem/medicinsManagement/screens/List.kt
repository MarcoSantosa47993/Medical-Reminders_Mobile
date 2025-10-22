package com.example.privateSystem.medicinsManagement.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.medicinsManagement.viewmodel.MedicinsListScreenViewModel
import com.example.shared.BaseState
import com.example.shared.components.MyAddButton
import com.example.shared.components.MySearchInput
import com.example.shared.components.listItem.ListItemPrimary
import com.example.shared.utils.EventBus
import com.example.shared.utils.Paths
import com.example.shared.utils.UiPrivateEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinsListScreen(
    medicinsRef: String,
) {
    val extras = MutableCreationExtras().apply {
        set(MedicinsListScreenViewModel.K_COLLECTION_REF, medicinsRef)
    }
    val scope = rememberCoroutineScope()

    val viewModel: MedicinsListScreenViewModel = viewModel(
        factory = MedicinsListScreenViewModel.Factory,
        extras = extras
    )

    val listState = viewModel.listState.observeAsState()
    val searchValue = viewModel.searchValue
    val filteredList = viewModel.filteredList

    LaunchedEffect(Unit) {
        val name = viewModel.getDependentName(medicinsRef) ?: "Dependent Name"
        EventBus.sendEvent(
            UiPrivateEvent.ChangeTitles(
                "Medicines",
                name,
                true
            )
        )
    }

    Column {
        MySearchInput(value = searchValue, onValueChange = {
            viewModel.onSearch(it)
        })

        PullToRefreshBox(
            isRefreshing = listState.value is BaseState.Loading,
            onRefresh = {
                viewModel.getMedicins()
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) {
                    ListItemPrimary(
                        title = it.name,
                        onClick = {
                            scope.launch {
                                EventBus.sendEvent(
                                    UiPrivateEvent.Navigate(
                                        Paths.PrivateSystem.DependentsSystem.MedicinesSystem.Details(
                                            medicinsRef,
                                            it.id
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    MyAddButton(
        onClick = {
            scope.launch {
                EventBus.sendEvent(
                    UiPrivateEvent.Navigate(
                        Paths.PrivateSystem.DependentsSystem.MedicinesSystem.Details(
                            medicinsRef,
                            null
                        )
                    )
                )
            }
        }

    )
}
