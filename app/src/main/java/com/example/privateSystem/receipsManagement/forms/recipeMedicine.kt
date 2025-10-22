package com.example.privateSystem.receipsManagement.forms

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.receipsManagement.events.RecipeMedicineFormEvent
import com.example.privateSystem.receipsManagement.models.RecipeMedicine
import com.example.privateSystem.receipsManagement.models.RecipeMedicineDialogState
import com.example.privateSystem.receipsManagement.states.RecipeMedicineFormState
import com.example.privateSystem.receipsManagement.viewmodel.RecipeMedicineFormViewModel
import com.example.shared.components.Label
import com.example.shared.components.MyAlertDialog
import com.example.shared.models.ValidationEvent

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RecipeMedicineForm(
    medicines: List<RecipeMedicine> = emptyList(),
    onAddMedicine: (RecipeMedicine) -> Unit,
    onRemoveMedicine: (String) -> Unit,
    onAdministrateMedicine: (RecipeMedicine) -> Unit,
    onEditMedicine: (RecipeMedicine) -> Unit
) {
    var toRemove by remember { mutableStateOf<RecipeMedicine?>(null) }
    var toAdministrate by remember { mutableStateOf<RecipeMedicine?>(null) }
    var dialogState by remember {
        mutableStateOf(RecipeMedicineDialogState(isOpen = false, data = null, readOnly = false))
    }

    Label("Recipe Medicines", actions = {

        FilledIconButton(onClick = {
            dialogState = RecipeMedicineDialogState(isOpen = true, data = null, readOnly = false)
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    })

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        medicines.forEach { med ->
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.White),
                headlineContent = { Text(med.shortDescription) },
                supportingContent = { Text(med.medicineName) },
                trailingContent = {
                    Row {
                        IconButton(onClick = {
                            dialogState = RecipeMedicineDialogState(
                                isOpen = true,
                                data = med,
                                readOnly = med.isAdministrated
                            )
                        }) {
                            Icon(Icons.Default.Visibility, contentDescription = "View/Edit")
                        }
                        IconButton(
                            onClick = { toRemove = med },
                            enabled = !med.isAdministrated,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = if (med.isAdministrated)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                else
                                    MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove")
                        }
                        FilledTonalIconButton(
                            onClick = { toAdministrate = med },
                            enabled = !med.isAdministrated
                        ) {
                            Icon(Icons.Default.Archive, contentDescription = "Manage")
                        }
                    }
                }
            )
            HorizontalDivider()
        }
    }

    if (toRemove != null) {
        MyAlertDialog(
            icon = Icons.Default.Medication,
            dialogTitle = "Delete Medicin",
            dialogText = "Are you sure you want to remove this medicin?",
            variant = "error",
            confirmText = "Delete",
            onConfirmation = {
                onRemoveMedicine(toRemove!!.id)
                toRemove = null
            },
            onDismissRequest = { toRemove = null }
        )
    } else if (toAdministrate != null) {
        MyAlertDialog(
            icon = Icons.Default.Archive,
            dialogTitle = "Administrate Medicin",
            dialogText = "Administrating this medicin, the medicin's quantity of the Dependent will be updated",
            variant = "info",
            confirmText = "Administrate",
            onConfirmation = {
                onAdministrateMedicine(toAdministrate!!)
                toAdministrate = null
            },
            onDismissRequest = { toAdministrate = null }
        )
    } else if (dialogState.isOpen) {
        MedicineForm(
            data = dialogState.data ?: RecipeMedicine.empty,
            readOnly = dialogState.readOnly,
            onDismissRequest = { dialogState = RecipeMedicineDialogState(false, null, false) },
            onSaveRequest = { med ->
                if (med.id.isEmpty()) onAddMedicine(med)
                else onEditMedicine(med)

                dialogState = RecipeMedicineDialogState(false, null, false)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineForm(
    data: RecipeMedicine,
    readOnly: Boolean,
    onDismissRequest: () -> Unit,
    onSaveRequest: (RecipeMedicine) -> Unit
) {
    val viewModel = viewModel<RecipeMedicineFormViewModel>()
    val state = viewModel.state


    LaunchedEffect(data) {
        viewModel.onEvent(RecipeMedicineFormEvent.MedicineNameChanged(data.medicineName))
        viewModel.onEvent(RecipeMedicineFormEvent.ShortDescriptionChanged(data.shortDescription))
        viewModel.onEvent(RecipeMedicineFormEvent.QuantityChanged(data.quantity.toString()))
    }


    LaunchedEffect(viewModel.validationEvents) {
        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val d = event.data as RecipeMedicineFormState
                val updated = data.copy(
                    medicineName = d.medicineName,
                    shortDescription = d.shortDescription,
                    quantity = d.quantity.toInt()
                )
                onSaveRequest(updated)
            }
        }
    }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismissRequest,
        title = { Text(if (data.id.isEmpty()) "Add Medicine" else "Medicine Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.medicineName,
                    onValueChange = {
                        viewModel.onEvent(
                            RecipeMedicineFormEvent.MedicineNameChanged(
                                it
                            )
                        )
                    },
                    label = { Text("Medicine Name") },
                    enabled = !readOnly,
                    isError = state.medicineNameError != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Medication,
                            contentDescription = "Medicine icon"
                        )
                    }
                )
                OutlinedTextField(
                    value = state.shortDescription,
                    onValueChange = {
                        viewModel.onEvent(
                            RecipeMedicineFormEvent.ShortDescriptionChanged(
                                it
                            )
                        )
                    },
                    prefix = {
                        Icons.Default.InsertDriveFile
                    },
                    label = { Text("Short Description") },
                    enabled = !readOnly,
                    isError = state.shortDescriptionError != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.InsertDriveFile,
                            contentDescription = "Description icon"
                        )
                    }
                )
                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = { viewModel.onEvent(RecipeMedicineFormEvent.QuantityChanged(it)) },
                    label = { Text("Quantity") },
                    enabled = !readOnly,
                    prefix = {
                        Icons.Default.Medication
                    },
                    isError = state.quantityError != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.InsertDriveFile,
                            contentDescription = "Description icon"
                        )
                    }
                )
            }
        },
        confirmButton = {
            if (!readOnly) {
                Button(onClick = { viewModel.onEvent(RecipeMedicineFormEvent.Submit) }) {
                    Text(if (data.id.isEmpty()) "Add" else "Update")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Back")
            }
        }
    )
}
