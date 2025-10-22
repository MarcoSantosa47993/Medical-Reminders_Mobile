package com.example.shared.models

import com.example.shared.enums.MyUserRoles
import com.example.shared.utils.AppWrite
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import kotlin.random.Random

data class MyUser(
    var id: String,
    var name: String,
    var email: String,
    var birthday: Date,
    var location: String,
    var phone: String,
    var phone2: String,
    var role: MyUserRoles,
    var pinCode: Long,
    var image: String?,
) {

    companion object {
        val empty = MyUser(
            id = "",
            name = "",
            email = "",
            birthday = Date(),
            location = "",
            phone = "",
            phone2 = "",
            role = MyUserRoles.dependent,
            pinCode = generateRandomPincode().toLong(),
            image = null
        )

        fun generateRandomPincode(): Int {
            // Generates a number from 1000 to 9999 inclusive
            return 1000 + Random.nextInt(9000)
        }

        fun fromDocument(documentSnapshot: DocumentSnapshot): MyUser {
            return MyUser(
                id = documentSnapshot.id,
                name = documentSnapshot.get("name") as String,
                email = documentSnapshot.get("email") as String,
                birthday = (documentSnapshot.get("birthday") as Timestamp).toDate(),
                location = documentSnapshot.get("location") as String,
                phone = documentSnapshot.get("phone") as String,
                phone2 = documentSnapshot.get("phone2") as String,
                role = MyUserRoles.entries[(documentSnapshot.get("role") as Long).toInt()],
                pinCode = documentSnapshot.get("pinCode") as Long,
                image = documentSnapshot.get("image") as String?
            )
        }
    }

    fun toDocument(): HashMap<String, *> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "birthday" to birthday,
            "location" to location,
            "phone" to phone,
            "phone2" to phone2,
            "role" to role.ordinal,
            "pinCode" to pinCode,
            "image" to image
        )
    }

    suspend fun getImage(): ByteArray? {
        image?.let { return AppWrite.getFile(it) }
        return null
    }


}