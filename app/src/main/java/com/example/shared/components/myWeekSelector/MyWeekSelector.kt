package com.example.shared.components.myWeekSelector

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.components.MyDateInput
import com.example.shared.components.myWeekInputFormat
import com.example.shared.utils.getWeekRangeByDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MyWeekSelector(
    modifier: Modifier = Modifier,
    currentWeek: Pair<LocalDate, LocalDate>,
    onSelectWeek: (newWeek: Pair<LocalDate, LocalDate>) -> Unit,
    onSelectDay: (newDate: LocalDate) -> Unit,
    currentDay: LocalDate,
) {
    Column {
        MyDateInput(
            label = {
                Text("Week")
            },
            value = myWeekInputFormat(currentWeek),
            disableClear = true,
            onValueChange = {
                if (it != null) {
                    val zoneId = ZoneId.systemDefault()
                    val instant = Instant.ofEpochMilli(it)
                    val localDate = LocalDate.ofInstant(instant, zoneId)
                    val newWeek = getWeekRangeByDate(localDate)
                    onSelectWeek(newWeek)
                    onSelectDay(newWeek.first)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        WeekDaySelector(
            selectedDay = currentDay,
            onDaySelected = onSelectDay,
            week = currentWeek
        )
    }
}