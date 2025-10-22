package com.example.privateSystem.components.card

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HealthDataCard(
    label: String,
    value: Double,
    unit: String,
    image: Bitmap?,
    onLongClick: (() -> Unit),
    onClick: (() -> Unit),
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color(0xFFCCE8E7), shape = RoundedCornerShape(20.dp))
            .padding(12.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "$value$unit",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(label)

            Spacer(modifier = Modifier.weight(1f))



            image?.let {
                Image(
                    bitmap = it.copy(Bitmap.Config.HARDWARE, false).asImageBitmap(),
                    contentDescription = label,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.End)
                )
            } ?: AsyncImage(
                "https://icons.veryicon.com/png/o/business/circular-multi-color-function-icon/health-health.png",
                contentDescription = label,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.End)
            )
        }
    }
}
