package com.example.privateSystem.receipsManagement.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import java.util.UUID


data class Recipe(
    val id: String,
    val number: String,
    val emittedAt: Date,
    val expiresAt: Date,
    val medicines: List<RecipeMedicine>
) {
    companion object {
        val empty = Recipe(
            id = "",
            number = "",
            emittedAt = Date(),
            expiresAt = Date(),
            medicines = emptyList()
        )

        fun fromDocument(doc: DocumentSnapshot): Recipe {
            val receiptNum = doc.getString("receiptNumber") ?: ""
            val emittedTs = doc.getTimestamp("emittedDate")?.toDate()?.time ?: 0L
            val expireTs = doc.getTimestamp("expireDate")?.toDate()?.time ?: 0L


            val medsRaw = doc.get("medications") as? List<Map<String, Any>> ?: emptyList()
            val meds = medsRaw.map { m ->
                RecipeMedicine(
                    id = (m["id"] as? String) ?: UUID.randomUUID().toString(),  // lê o id do mapa
                    medicineName = m["name"] as? String ?: "",
                    shortDescription = m["observations"] as? String ?: "",
                    quantity = (m["quantityPurchased"] as? Number)?.toInt() ?: 0,
                    isAdministrated = (m["isAdministered"]  as? Boolean) ?: false
                )
            }

            return Recipe(
                id = doc.id,
                number = receiptNum,
                emittedAt = Date(emittedTs),
                expiresAt = Date(expireTs),
                medicines = meds
            )
        }
    }

    fun toMap(): Map<String, Any> = mapOf(
        "receiptNumber" to number,
        "emittedDate" to emittedAt,
        "expireDate" to expiresAt,
        "medications" to medicines.map { m ->
            mapOf(
                "id" to m.id,
                "name" to m.medicineName,
                "observations" to m.shortDescription,
                "quantityPurchased" to m.quantity,
                "isAdministered"    to m.isAdministrated
            )
        }
    )
}
