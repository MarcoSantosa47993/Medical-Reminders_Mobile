package com.example.privateSystem.planning.states

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shared.enums.PlanPriority
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class PlanFormState(
    val medicineId: String = "",
    val medicineIdError: String? = null,
    val takeAt: LocalDateTime = LocalDateTime.now(),
    val takeAtError: String? = null,
    val priority: PlanPriority = PlanPriority.LOW,
    val priorityError: String? = null,
    val emptyStomach: Boolean = false,
    val emptyStomachError: String? = null,
)