package com.example.privateSystem.dependentsManagement.screens.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.components.MyToolbarButton
import com.example.shared.models.ToolbarItem
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import kotlinx.coroutines.launch

@Composable
fun DependentToolbar(dependentRef: String) {
    val scope = rememberCoroutineScope()

    var toolItemsDependent = listOf(
        ToolbarItem.Health(dependentRef),
        ToolbarItem.Receips(dependentRef),
        ToolbarItem.Planning(dependentRef),
        ToolbarItem.Medicins(dependentRef),
    )

    LazyRow(
        modifier = Modifier
            .width(393.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(toolItemsDependent) { index, it ->

            MyToolbarButton(it.title, onClick = {
                if (it.route != null) {
                    scope.launch {
                        EventBus.sendEvent(UiPrivateEvent.Navigate(it.route))
                    }
                }
            }, imageVector = it.icon)
        }


    }
}