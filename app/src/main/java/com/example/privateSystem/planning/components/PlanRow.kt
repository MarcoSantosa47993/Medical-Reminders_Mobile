package com.example.privateSystem.planning.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shared.enums.PlanStatus
import com.example.shared.models.Plan
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
private val formatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlanRow(
    plan: Plan,
    castToMedicineName: (medicineId: String) -> String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val zoneId = ZoneId.systemDefault()
    val takeAt = LocalDateTime.ofInstant(plan.takeAt.toInstant(), zoneId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(castToMedicineName(plan.medicineId), Modifier.weight(6f / 12f))
        Text(formatter.format(takeAt), Modifier.weight(3f / 12f), textAlign = TextAlign.Center)

        Icon(
            Icons.Rounded.Circle, contentDescription = null, tint = when (plan.taked) {
                PlanStatus.PENDING -> Color.Gray
                PlanStatus.TAKED -> Color.Green
                PlanStatus.NOT_TAKED -> Color.Red
            }, modifier = Modifier.weight(3f / 12f)
        )
    }
}