package com.example.shared.components.myWeekSelector

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekDaySelector(
    week: Pair<LocalDate, LocalDate>,
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
) {


    val dates = generateSequence(week.first) { current ->
        if (current < week.second) current.plusDays(1) else null
    }.toList()


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dates.forEach {
            MyDayItem(
                selected = it == selectedDay,
                onSelect = {
                    onDaySelected(it)
                },
                date = it
            )
        }
    }
}


