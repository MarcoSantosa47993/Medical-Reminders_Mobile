package com.example.shared.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shared.utils.AppWrite
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate
import java.time.ZoneId

data class Health(
    var id: String,
    var label: String,
    var value: Double,
    var unit: String,
    var image: String?,
    var date: LocalDate,
) {
    suspend fun getImage(): ByteArray? {
        image?.let { return AppWrite.getFile(it) }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDocument(): HashMap<String, *> {
        val zoneId = ZoneId.systemDefault()
        return hashMapOf(
            "id" to id,
            "label" to label,
            "value" to value,
            "unit" to unit,
            "image" to image,
            "date" to Timestamp(time = date.atTime(0, 0).atZone(zoneId).toInstant())
        )
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val empty = Health(
            id = "", label = "", value = 0.0, unit = "", image = null, date = LocalDate.now()
        )

        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        fun fromDocument(documentSnapshot: DocumentSnapshot): Health {
            return Health(
                id = documentSnapshot.id,
                label = documentSnapshot.get("label") as String,
                value = documentSnapshot.getDouble("value") ?: 0.0,
                unit = documentSnapshot.get("unit") as String,
                image = documentSnapshot.get("image") as String?,
                date = LocalDate.ofInstant(
                    (documentSnapshot.get("date") as Timestamp).toInstant(), ZoneId.systemDefault()
                )
            )
        }
    }
}