package com.example.privateSystem.components.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shared.enums.PlanStatus

@Composable
fun CustomStep(status: PlanStatus) {

    val color = when (status) {
        PlanStatus.PENDING -> MaterialTheme.colorScheme.outline
        PlanStatus.TAKED -> MaterialTheme.colorScheme.primary
        PlanStatus.NOT_TAKED -> MaterialTheme.colorScheme.error
    }

    val icon = when (status) {
        PlanStatus.PENDING -> Icons.Rounded.Timelapse
        PlanStatus.TAKED -> Icons.Rounded.Check
        PlanStatus.NOT_TAKED -> Icons.Rounded.Warning
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}