package com.example.shared.components

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyWeekInput(
    value: Pair<Long?, Long?>,
    onChangeValue: (Pair<Long?, Long?>) -> Unit,
) {
    var showCalendar by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = myWeekInputFormat(value),
        onValueChange = {},
        label = { Text(text = "Select Week Range") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null
            )
        },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showCalendar = true }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Open Calendar"
                )
            }
        }
    )


    when {
        showCalendar -> {
            MyDateRangePickerModal(
                onDateRangeSelected = onChangeValue,
                onDismiss = {
                    showCalendar = false
                }
            )
        }
    }
}*/

@RequiresApi(Build.VERSION_CODES.O)
fun myWeekInputFormat(value: Pair<LocalDate, LocalDate>): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val formattedFirst = value.first.format(formatter)
    val formattedSecond = value.second.format(formatter)

    return "$formattedFirst - $formattedSecond"
}