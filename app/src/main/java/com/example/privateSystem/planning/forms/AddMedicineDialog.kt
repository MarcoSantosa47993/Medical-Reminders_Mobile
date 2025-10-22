package com.example.privateSystem.planning.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        medicine: String,
        takeAt: String,
        priority: String,
        fast: Boolean,
        quantity: String
    ) -> Unit
) {
    val medicineOptions = listOf("Paracetamol", "Brufen", "Aspirina")
    val priorityOptions = listOf("High", "Medium", "Low")

    var expandedMedicine by remember { mutableStateOf(false) }
    var selectedMedicine by remember { mutableStateOf(medicineOptions.first()) }

    var takeAt by remember { mutableStateOf("") }

    var expandedPriority by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(priorityOptions.first()) }

    var fast by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = { Text("Add Medicin To Plan") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedMedicine,
                    onExpandedChange = { expandedMedicine = !expandedMedicine }
                ) {
                    OutlinedTextField(
                        value = selectedMedicine,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Medicine Name") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMedicine) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMedicine,
                        onDismissRequest = { expandedMedicine = false }
                    ) {
                        medicineOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedMedicine = option
                                    expandedMedicine = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = takeAt,
                    onValueChange = { takeAt = it },
                    label = { Text("Take At") },
                    trailingIcon = {
                        IconButton(onClick = { /* TODO: abrir TimePicker */ }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select time")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedPriority,
                    onExpandedChange = { expandedPriority = !expandedPriority }
                ) {
                    OutlinedTextField(
                        value = selectedPriority,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPriority) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPriority,
                        onDismissRequest = { expandedPriority = false }
                    ) {
                        priorityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedPriority = option
                                    expandedPriority = false
                                }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Empty Stomach")
                        Switch(
                            checked = fast,
                            onCheckedChange = { fast = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(selectedMedicine, takeAt, selectedPriority, fast, quantity)
                },
                modifier = Modifier.height(48.dp)
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Back")
            }
        }
    )
}
