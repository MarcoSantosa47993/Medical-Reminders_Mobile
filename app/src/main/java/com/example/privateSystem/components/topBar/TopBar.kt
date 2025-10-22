package com.example.privateSystem.components.topBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.medicinsschedules.R
import com.example.shared.utils.EventBus
import com.example.shared.utils.UiPrivateEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    subtitle: String?,
    backButton: Boolean = false
) {
    val scope = rememberCoroutineScope()
    if (backButton) {
        CenterAppBar(title, subtitle, onBackPress = {
            scope.launch {
                EventBus.sendEvent(UiPrivateEvent.NavigateBack)
            }
        })
    } else {
        LeftAppBar(title, subtitle)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CenterAppBar(title: String, subtitle: String?, onBackPress: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(end = 5.dp, start = 5.dp),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title)
                if (!subtitle.isNullOrEmpty()) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.height(40.dp)
            )
        },
        navigationIcon = {
            FilledIconButton(
                onClick = {
                    if (onBackPress != null) {
                        onBackPress()
                    }
                }, colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeftAppBar(title: String, subtitle: String?) {
    TopAppBar(
        modifier = Modifier.padding(end = 10.dp),
        title = {
            Column {
                Text(title)
                if (!subtitle.isNullOrEmpty()) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.height(40.dp)
            )
        }
    )
}