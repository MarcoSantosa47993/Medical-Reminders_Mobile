package com.example.privateSystem.medicinsManagement.models

import android.util.Log
import com.example.authSystem.models.Period
import com.google.firebase.firestore.DocumentSnapshot

data class Medicin(
    val id: String,
    var name : String,
    var type : String,
    var quantity : Int,
    var dosePerPeriod : Int,
    var period : Period,
    var observations : String
) {

    companion object {
        val empty = Medicin(
            "",
            "",
            "",
            1,
            1,
            Period.DAY,
            ""
        )

        fun fromDocument(documentSnapshot: DocumentSnapshot): Medicin {
            val periodOrdinal = documentSnapshot.getLong("period")?.toInt() ?: 0
            val period = Period.entries.toTypedArray().getOrElse(periodOrdinal) { Period.DAY }
            Log.d("Period", period.toString());
            return Medicin(
                id = documentSnapshot.id,
                name = documentSnapshot.get("name") as String,
                type = documentSnapshot.get("type") as String,
                quantity = documentSnapshot.getLong("quantity")?.toInt() ?: 1,
                dosePerPeriod = documentSnapshot.getLong("dosePerPeriod")?.toInt() ?: 1,
                period = Period.entries[documentSnapshot.getLong("period")?.toInt() ?: 0],
                observations = documentSnapshot.get("observations") as String
            )
        }
    }


    fun toDocument(): HashMap<String, *> {
        return hashMapOf(
            "name" to name,
            "type" to type,
            "quantity" to quantity,
            "dosePerPeriod" to dosePerPeriod,
            "period" to period.ordinal,
            "observations" to observations
        )
    }
}