package com.example.privateSystem.models

import java.text.SimpleDateFormat
import java.util.Locale

data class User(
    var name : String,
    var birthDay : Long,
    var location : String,
    var primaryPhone : Int,
    var secondaryPhone : Int
)


val users = listOf(
    User(
        name = "Manuel Fernandes",
        birthDay = parseDateToMillis("12/03/1923"),
        location = "Alfândega da Fé, Bragança",
        primaryPhone = 912345678,
        secondaryPhone = 932112345
    ),
    User(
        name = "Manuela da Conceição",
        birthDay = parseDateToMillis("12/03/1963"),
        location = "Porto",
        primaryPhone = 913456789,
        secondaryPhone = 964123456
    ),
)

fun parseDateToMillis(dateString: String): Long {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.parse(dateString)?.time ?: 0
}