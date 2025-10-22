package com.example.privateSystem.receipsManagement.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.receipsManagement.viewmodel.ReceipsViewModel
import com.example.shared.components.MyAddButton
import com.example.shared.components.MySearchInput
import com.example.shared.components.listItem.ListItemPrimary
import com.example.shared.utils.EventBus
import com.example.shared.utils.Paths
import com.example.shared.utils.UiPrivateEvent
import kotlinx.coroutines.launch

@Composable
fun ListReceipsScreen(
    receiptsRef: String,
) {

    val extras = MutableCreationExtras().apply {
        set(ReceipsViewModel.K_COLLECTION_REF, receiptsRef)
    }
    val vm: ReceipsViewModel = viewModel(
        factory = ReceipsViewModel.Factory,
        extras = extras
    )

    val scope = rememberCoroutineScope()
    var search by remember { mutableStateOf("") }


    val list = vm.recipes


    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.fetchRecipes()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MySearchInput(
            value = search,
            onValueChange = { search = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items(list.filter { r ->
                r.number.contains(search, ignoreCase = true)
            }) { r ->
                ListItemPrimary(
                    title = r.number,
                    onClick = {
                        scope.launch {
                            EventBus.sendEvent(
                                UiPrivateEvent.Navigate(
                                    Paths.PrivateSystem.DependentsSystem.RecipesSystem.Details(
                                        receiptsRef,
                                        r.id
                                    )
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    MyAddButton(onClick = {
        scope.launch {
            EventBus.sendEvent(
                UiPrivateEvent.Navigate(
                    Paths.PrivateSystem.DependentsSystem.RecipesSystem.Details(
                        receiptsRef,
                        null
                    )
                )
            )
        }
    })
}