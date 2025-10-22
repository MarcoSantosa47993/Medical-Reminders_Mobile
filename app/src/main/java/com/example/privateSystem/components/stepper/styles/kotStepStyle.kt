package com.example.privateSystem.components.stepper.styles

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binayshaw7777.kotstep.v3.model.step.StepLayoutStyle
import com.binayshaw7777.kotstep.v3.model.style.BorderStyle
import com.binayshaw7777.kotstep.v3.model.style.KotStepStyle
import com.binayshaw7777.kotstep.v3.model.style.LineStyle
import com.binayshaw7777.kotstep.v3.model.style.LineStyles
import com.binayshaw7777.kotstep.v3.model.style.StepStyle
import com.binayshaw7777.kotstep.v3.model.style.StepStyles
import com.binayshaw7777.kotstep.v3.util.ExperimentalKotStep

@OptIn(ExperimentalKotStep::class)
fun kotStepStyle(colorScheme: ColorScheme): KotStepStyle {
    return KotStepStyle(
        stepLayoutStyle = StepLayoutStyle.Vertical,
        showCheckMarkOnDone = false,
        ignoreCurrentState = false,
        stepStyle = StepStyles.default().copy(
            onTodo = StepStyle.defaultTodo().copy(
                stepSize = 32.dp,
                stepColor = Color.Transparent,
                borderStyle = BorderStyle(width = 1.dp, color = Color(0xFFB3B3B3)),
                textStyle = TextStyle(
                    color = Color(0xFFB3B3B3),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

            ),
            onCurrent = StepStyle.defaultCurrent().copy(
                stepSize = 34.dp,
                stepColor = Color.Transparent,
                borderStyle = BorderStyle(
                    width = 0.dp,
                    color = Color.Transparent
                )
            ),
            onDone = StepStyle.defaultDone().copy(
                stepSize = 32.dp,
                stepColor = colorScheme.primary,
                textStyle = TextStyle(
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )

        ),

        lineStyle = LineStyles.default().copy(
            onTodo = LineStyle.defaultTodo().copy(
                lineThickness = 1.5.dp,
                lineLength = 32.dp,
                linePadding = PaddingValues(2.dp),
                lineColor = colorScheme.primary
            ),
            onCurrent = LineStyle.defaultCurrent().copy(
                lineThickness = 1.5.dp,
                lineLength = 32.dp,
                linePadding = PaddingValues(2.dp),
                lineColor = colorScheme.primary
            ),
            onDone = LineStyle.defaultDone().copy(
                lineThickness = 1.5.dp,
                lineLength = 32.dp,
                linePadding = PaddingValues(2.dp),
                lineColor = colorScheme.primary,
                progressColor = colorScheme.primary
            )
        )
    )
}