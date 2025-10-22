package com.example.privateSystem.receipsManagement.forms

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privateSystem.receipsManagement.events.RecipeDataFormEvent
import com.example.privateSystem.receipsManagement.models.Recipe
import com.example.privateSystem.receipsManagement.states.RecipeDataFormState
import com.example.privateSystem.receipsManagement.viewmodel.RecipeDataFormViewModel
import com.example.shared.components.Label
import com.example.shared.components.MyDateInput
import com.example.shared.models.ValidationEvent
import com.example.shared.utils.convertMillisToDate
import java.util.Date

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RecipeDetailForm(
    modifier: Modifier = Modifier,
    currentRecipe: Recipe,
    onSaveRequest: (Recipe) -> Unit
) {
    val viewModel = viewModel<RecipeDataFormViewModel>()
    val state = viewModel.state
    val context = LocalContext.current


    LaunchedEffect(currentRecipe) {
        viewModel.onEvent(RecipeDataFormEvent.RecipeNumberChanged(currentRecipe.number))
        viewModel.onEvent(RecipeDataFormEvent.EmittedAtChanged(currentRecipe.emittedAt.time))
        viewModel.onEvent(RecipeDataFormEvent.ExpiresAtChanged(currentRecipe.expiresAt.time))
    }


    LaunchedEffect(context) {
        viewModel.validationEvents.collect { event ->
            if (event is ValidationEvent.Success<*>) {
                val data = event.data as RecipeDataFormState
                val updatedRecipe = currentRecipe.copy(
                    number = data.recipeNumber,
                    emittedAt = Date(data.emittedAt!!),
                    expiresAt = Date(data.expiresAt!!),
                    medicines = currentRecipe.medicines
                )
                onSaveRequest(updatedRecipe)
                viewModel.clearErrors()
            }
        }
    }

    Label("Recipe Data")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.recipeNumber,
            onValueChange = { viewModel.onEvent(RecipeDataFormEvent.RecipeNumberChanged(it)) },
            label = { Text("Recipe Number") },
            isError = state.recipeNumberError != null,
            modifier = Modifier.fillMaxWidth()
        )

        MyDateInput(
            value = state.emittedAt?.let { convertMillisToDate(it) } ?: "",
            onValueChange = { newMillis: Long? ->
                viewModel.onEvent(
                    RecipeDataFormEvent.EmittedAtChanged(
                        newMillis
                    )
                )
            },
            label = { Text("Emitted Date") },
            isError = state.emittedAtError != null
        )

        MyDateInput(
            value = state.expiresAt?.let { convertMillisToDate(it) } ?: "",
            onValueChange = { newMillis: Long? ->
                viewModel.onEvent(
                    RecipeDataFormEvent.ExpiresAtChanged(
                        newMillis
                    )
                )
            },
            label = { Text("Expiry Date") },
            isError = state.expiresAtError != null
        )

        ElevatedButton(
            onClick = { viewModel.onEvent(RecipeDataFormEvent.Sumbit) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (currentRecipe.id.isEmpty()) "Create" else "Update")
        }
    }
}
