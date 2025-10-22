package com.example.shared.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun millisToISOString(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC for ISO format
    return sdf.format(Date(millis))
}

fun isoToDate(isoString: String): String {
    if (isoString != "") {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure UTC time

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = isoFormat.parse(isoString) // Parse the ISO string
        return outputFormat.format(date!!) // Convert to YYYY-MM-dd
    }

    return ""
}

fun parseDateToMillis(dateString: String): Long? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "PT"))
        formatter.parse(dateString)?.time
    } catch (e: Exception) {
        null
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "PT"))
    return formatter.format(Date(millis))
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun getCurrentWeekRange(): Pair<LocalDate, LocalDate> {
    val date = LocalDate.now()
    return getWeekRangeByDate(date)
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun getWeekRangeByDate(dateTime: LocalDate): Pair<LocalDate, LocalDate> {
    val startOfWeek =
        dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val endOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))


    return Pair(startOfWeek, endOfWeek)
}