package com.example.privateSystem.planning.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.authSystem.models.Period
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun getPeriodRangeByDate(date: LocalDate, period: Period): Pair<Instant, Instant> {
    val zoneId = ZoneId.systemDefault()
    var firstDate = LocalDateTime.now()
    var lastDate = LocalDateTime.now()

    when (period) {
        Period.DAY -> {
            firstDate = date.atStartOfDay().plusHours(1)
            lastDate = date.atTime(LocalTime.MAX).plusHours(1)
        }

        Period.WEEK -> {
            val x = date.with(DayOfWeek.SUNDAY)
            val y = date.with(DayOfWeek.SATURDAY)

            firstDate = x.atStartOfDay().plusHours(1)
            lastDate = y.atTime(LocalTime.MAX).plusHours(1)
        }

        Period.MONTH -> {
            val x = date.withDayOfMonth(1)
            val y = date.withDayOfMonth(date.lengthOfMonth())

            firstDate = x.atStartOfDay().plusHours(1)
            lastDate = y.atTime(LocalTime.MAX).plusHours(1)
        }

        Period.YEAR -> {
            val x = date.withDayOfYear(1)
            val y = date.withDayOfYear(date.lengthOfYear())
            firstDate = x.atStartOfDay().plusHours(1)
            lastDate = y.atTime(LocalTime.MAX).plusHours(1)
        }
    }

    return Pair(firstDate.atZone(zoneId).toInstant(), lastDate.atZone(zoneId).toInstant())
}