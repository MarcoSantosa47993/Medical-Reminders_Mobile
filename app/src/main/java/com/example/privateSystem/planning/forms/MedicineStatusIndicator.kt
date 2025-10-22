package com.example.privateSystem.planning.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicinsschedules.ui.theme.errorLight
import com.example.medicinsschedules.ui.theme.onPrimaryLight
import com.example.medicinsschedules.ui.theme.outlineLight
import com.example.privateSystem.planning.models.MedicineStatus

@Composable
fun MedicineStatusIndicator(
    status: MedicineStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .border(
                width = 2.dp,
                color = when (status) {
                    MedicineStatus.NOT_TAKEN -> outlineLight
                    MedicineStatus.NOT_DONE -> errorLight
                    MedicineStatus.DONE -> Color(0xFF016630)
                },
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color =  Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            MedicineStatus.DONE -> Icon(
                Icons.Default.Check,
                contentDescription = "Taken",
                tint = Color(0xFF016630),
                modifier = Modifier.size(20.dp)
            )
            MedicineStatus.NOT_DONE -> Text(
                "×",
                color = errorLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            MedicineStatus.NOT_TAKEN -> Unit
        }
    }
}