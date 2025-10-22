package com.example.authSystem.models

enum class Period(val displayName: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year");

    override fun toString(): String = displayName
}