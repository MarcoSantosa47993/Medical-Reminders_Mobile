package com.example.privateSystem.planning.events

import com.example.shared.enums.PlanPriority
import java.time.LocalDateTime

sealed class PlanFormEvent {
    data class MedicineIdChanged(val value: String) : PlanFormEvent()
    data class TakeAtChanged(val value: LocalDateTime) : PlanFormEvent()
    data class PriorityChanged(val value: PlanPriority) : PlanFormEvent()
    data class EmptyStomachChanged(val value: Boolean) : PlanFormEvent()

    object Submit : PlanFormEvent()
}


