package com.example.privateSystem.models

import com.example.medicinsschedules.R
import com.example.shared.utils.parseDateToMillis

data class UserHealthData(
    var label: String,
    var value: Long,
    var unit: String,
    var quantity: Long,
    var registDate: Long,
    var imageUri: Int? = null
)

val usersHealthData = listOf(
    UserHealthData(
        label      = "Weight",
        value      = 70,
        unit       = "kg",
        quantity   = 1,
        registDate = parseDateToMillis("23/04/2025")!!,
        imageUri   = R.drawable.weight
    ),
    UserHealthData(
        label      = "Height",
        value      = 175,
        unit       = "cm",
        quantity   = 1,
        registDate = parseDateToMillis("23/04/2025")!!,
        imageUri   = null
    ),
    UserHealthData(
        label      = "Heart Rate",
        value      = 70,
        unit       = "bpm",
        quantity   = 1,
        registDate = parseDateToMillis("23/04/2025")!!,
        imageUri   = R.drawable.heartrate
    ),
    UserHealthData(
        label      = "Blood Sugar",
        value      = 154,
        unit       = "mg/dl",
        quantity   = 1,
        registDate = parseDateToMillis("23/04/2025")!!,
        imageUri   = R.drawable.diabetes
    )
)