package com.example.shared.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class Dependent(
    var id: String,
    var phone: String,
    var phone2: String,
    var userId: String,
    var birthday: Date,
    var location: String,
    var name: String,
) {
    companion object {
        val empty = Dependent(
            id = "",
            phone = "",
            phone2 = "",
            userId = "",
            birthday = Date(),
            location = "",
            name = ""
        )

        fun fromDocument(documentSnapshot: DocumentSnapshot): Dependent {
            return Dependent(
                id = documentSnapshot.id,
                phone = documentSnapshot.get("phone") as String,
                phone2 = documentSnapshot.get("phone2") as String,
                userId = documentSnapshot.get("userId") as String,
                birthday = (documentSnapshot.get("birthday") as Timestamp).toDate(),
                location = documentSnapshot.get("location") as String,
                name = documentSnapshot.get("name") as String
            )
        }
    }

    fun toDocument(): HashMap<String, *> {
        return hashMapOf(
            "name" to name,
            "birthday" to birthday,
            "location" to location,
            "phone" to phone,
            "phone2" to phone2,
            "userId" to userId
        )
    }

}