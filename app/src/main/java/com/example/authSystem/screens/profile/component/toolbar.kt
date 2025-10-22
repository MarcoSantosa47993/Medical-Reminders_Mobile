package com.example.authSystem.screens.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.components.MyToolbarButton
import com.example.shared.models.ToolbarItem

@Composable
fun ProfileToolbar(
    modifier: Modifier = Modifier,
    onShareProfileRequest: () -> Unit,
    onChangePasswordRequest: () -> Unit,
) {
    LazyRow(
        modifier = modifier
            .width(393.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            MyToolbarButton(
                ToolbarItem.ShareProfile.title,
                onClick = onShareProfileRequest,
                imageVector = ToolbarItem.ShareProfile.icon
            )
        }

        item {
            MyToolbarButton(
                ToolbarItem.ChangePassword.title,
                onClick = onChangePasswordRequest,
                imageVector = ToolbarItem.ChangePassword.icon
            )
        }
    }
}