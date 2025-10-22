package com.example.shared.models

import com.example.shared.enums.PlanPriority
import com.example.shared.enums.PlanStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class Plan(
    var id: String,
    var medicineId: String,
    var priority: PlanPriority,
    var quantity: Int,
    var takeAt: Date,
    var taked: PlanStatus,
    val jejum: Boolean,
) {
    companion object {
        val empty = Plan(
            id = "",
            medicineId = "",
            priority = PlanPriority.LOW,
            quantity = 1,
            takeAt = Date(),
            taked = PlanStatus.PENDING,
            jejum = false
        )

        fun fromDocument(documentSnapshot: DocumentSnapshot): Plan {
            return Plan(
                id = documentSnapshot.id,
                medicineId = documentSnapshot.get("medicineId") as String,
                priority = PlanPriority.entries[(documentSnapshot.get("priority") as Long).toInt()],
                quantity = (documentSnapshot.get("quantity") as Long).toInt(),
                takeAt = (documentSnapshot.get("takeAt") as Timestamp).toDate(),
                taked = PlanStatus.entries[(documentSnapshot.get("taked") as Long).toInt()],
                jejum = documentSnapshot.get("jejum") as Boolean
            )
        }
    }

    fun toDocument(): HashMap<String, *> {
        return hashMapOf(
            "id" to id,
            "medicineId" to medicineId,
            "priority" to priority.ordinal,
            "quantity" to quantity,
            "takeAt" to takeAt,
            "taked" to taked.ordinal,
            "jejum" to jejum
        )
    }
}