package com.example.privateSystem.receipsManagement.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.receipsManagement.forms.RecipeDetailForm
import com.example.privateSystem.receipsManagement.forms.RecipeMedicineForm
import com.example.privateSystem.receipsManagement.models.Recipe
import com.example.privateSystem.receipsManagement.models.RecipeMedicine
import com.example.privateSystem.receipsManagement.viewmodel.RecipeDetailsViewModel
import com.example.shared.components.MyAlertDialog
import com.example.shared.components.MyLoading

@Composable
fun ReceipDetailsScreen(
    receiptsRef: String,
    recipeId: String,
) {
    val isNew = recipeId.isBlank()
    val extras = MutableCreationExtras().apply {
        set(RecipeDetailsViewModel.RECEIPTS_REF, receiptsRef)
        set(RecipeDetailsViewModel.RECIPE_ID, if (isNew) null else recipeId)
    }

    val viewModel: RecipeDetailsViewModel = viewModel(
        factory = RecipeDetailsViewModel.Factory,
        extras = extras
    )
    val state by viewModel.recipeState

    if (isNew) {
        Content(
            data = Recipe.empty,
            state = null,
            viewModel = viewModel,
            onSave = { newRecipe ->
                viewModel.createReceipt(newRecipe)
            },
            onDelete = null
        )
    } else {
        if (state.loading) {
            MyLoading()
        } else {
            Content(
                data = state.data,
                state = state,
                viewModel = viewModel,
                onSave = { updated ->
                    viewModel.updateRecipe(updated)
                },
                onDelete = {
                    viewModel.deleteRecipe()
                }
            )
        }
    }
}

@Composable
private fun Content(
    data: Recipe,
    state: com.example.privateSystem.receipsManagement.models.RecipeState?,
    viewModel: RecipeDetailsViewModel,
    onSave: (Recipe) -> Unit,
    onDelete: (() -> Unit)?
) {
    val scroll = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(scroll)
    ) {
        RecipeDetailForm(
            currentRecipe = data,
            onSaveRequest = onSave
        )

        if (data.id.isNotEmpty()) {
            Box(modifier = Modifier.height(20.dp))
            RecipeMedicineForm(
                medicines = data.medicines,
                onAddMedicine = { med: RecipeMedicine ->
                    viewModel.addMedicine(med)
                },
                onRemoveMedicine = { medId: String ->
                    viewModel.removeMedicine(medId)
                },
                onEditMedicine = { med: RecipeMedicine ->
                    viewModel.editMedicine(med)
                },
                onAdministrateMedicine = { med: RecipeMedicine ->
                    viewModel.administrateMedicine(med)
                }
            )

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Remove Receipt")
            }
        }
    }

    if (showDeleteDialog && onDelete != null) {
        MyAlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {
                onDelete()
                showDeleteDialog = false
            },
            dialogTitle = "Delete Receipt",
            dialogText = "Are you sure you want to remove this receipt?",
            icon = Icons.Filled.FilePresent,
            variant = "error",
            confirmText = "Delete"
        )
    }
}
