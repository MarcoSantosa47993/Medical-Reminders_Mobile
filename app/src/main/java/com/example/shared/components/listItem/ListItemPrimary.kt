package com.example.shared.components.listItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ListItemPrimary(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    title: String,
) {
    val cornerRadius = RoundedCornerShape(12.dp)

    ListItem(
        headlineContent = {
            Text(title, fontWeight = FontWeight.SemiBold)
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null
            )
        },
        tonalElevation = 2.dp,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            trailingIconColor = MaterialTheme.colorScheme.primary,
            headlineColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = cornerRadius,
                clip = false
            )
            .clip(cornerRadius)
            .background(MaterialTheme.colorScheme.surface, shape = cornerRadius)
            .clickable(enabled = onClick != null) {
                onClick!!()
            }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ListItemPrimary(title = "Hello")
}