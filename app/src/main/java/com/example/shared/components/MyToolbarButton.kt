package com.example.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyToolbarButton(name: String, imageVector: ImageVector, onClick: () -> Unit) {

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),


        ) {

        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = name,
            color = MaterialTheme.colorScheme.tertiary,
            style = TextStyle(
                fontSize = 14.sp

            )
        )
    }


}
