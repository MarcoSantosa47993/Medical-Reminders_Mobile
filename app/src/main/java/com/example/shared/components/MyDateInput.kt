package com.example.shared.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.authSystem.modal.DatePickerModal

@Composable
fun MyDateInput(
    value: String,
    onValueChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    disableClear: Boolean = false,

    ) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = label,
        enabled = enabled,
        readOnly = true,
        isError = isError,
        leadingIcon = leadingIcon,
        placeholder = { Text("MM/DD/YYYY") },
        supportingText = supportingText,
        trailingIcon = {
            Row {
                if (value != "" && !disableClear) {
                    IconButton(onClick = {
                        onValueChange(null)
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear date")
                    }
                }

                IconButton(
                    onClick = {
                        showModal = true
                    }, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()

    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = onValueChange,
            onDismiss = { showModal = false }
        )
    }
}

