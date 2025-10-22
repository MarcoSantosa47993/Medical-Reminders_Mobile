package com.example.privateSystem.components.stepper

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.kotstep.v3.KotStep
import com.binayshaw7777.kotstep.v3.model.KotStepScope
import com.binayshaw7777.kotstep.v3.util.ExperimentalKotStep
import com.example.privateSystem.components.stepper.styles.kotStepStyle
import com.example.shared.models.Plan
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalKotStep::class)
@Composable
fun StepperItem(modifier: Modifier = Modifier, content: KotStepScope.() -> Unit) {
    val kotStepStyle = kotStepStyle(MaterialTheme.colorScheme)



    KotStep(
        modifier = Modifier
            .fillMaxWidth(),
        currentStep = { 0f },
        style = kotStepStyle

    ) {
        content()
        /*step(
            title = "1",
            label = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "09:00",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Griponal",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        )

        step(
            title = "2",
            label = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "10:00",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    Text(
                        text = "Paracetamol",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        )

        step(
            content = { CustomStep() },
            label = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "11:00",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    Text(
                        text = "Brufen",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        )

        step(
            title = "4",
            label = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "12:00",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    Text(
                        text = "Benuron",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                }

            })*/
    }
}

@OptIn(ExperimentalKotStep::class)
@RequiresApi(Build.VERSION_CODES.O)
fun KotStepScope.stepPlan(
    plan: Plan,
    index: Int,
    castToMedicineName: (medicineId: String) -> String,
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val zoneId = ZoneId.systemDefault()
    val takeAtLabel = formatter.format(LocalDateTime.ofInstant(plan.takeAt.toInstant(), zoneId))


    step(
        content = { CustomStep(plan.taked) },
        label = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = takeAtLabel,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                Text(
                    text = castToMedicineName(plan.medicineId),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

        }
    )
}