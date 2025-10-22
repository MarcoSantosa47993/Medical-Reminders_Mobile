package com.example.privateSystem.receipsManagement.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.privateSystem.receipsManagement.models.Recipe
import com.example.privateSystem.receipsManagement.models.RecipeMedicine
import com.example.privateSystem.receipsManagement.models.RecipeState
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class RecipeDetailsViewModel(
    private val receiptsRef: String,
    private val recipeId: String?
) : ViewModel() {

    private val db = Firebase.firestore
    private val col = db.collection(receiptsRef)
    private val recipeDoc = recipeId?.let { db.document("$receiptsRef/$it") }

    private val _recipeState = mutableStateOf(RecipeState())
    val recipeState: State<RecipeState> = _recipeState

    init {
        fetchRecipe()
    }

    private fun fetchRecipe() {
        if (recipeId.isNullOrBlank()) return
        _recipeState.value = _recipeState.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val snap = recipeDoc!!.get().await()
                if (snap.exists()) {
                    val recipe = Recipe.fromDocument(snap)
                    _recipeState.value = RecipeState(
                        loading = false,
                        data = recipe,
                        error = null
                    )
                } else {
                    _recipeState.value = RecipeState(
                        loading = false,
                        data = Recipe.empty,
                        error = "Recipe not found"
                    )
                }
            } catch (e: Exception) {
                _recipeState.value = RecipeState(
                    loading = false,
                    data = Recipe.empty,
                    error = e.message
                )
            }
        }
    }


    fun createReceipt(newReceipt: Recipe) {
        viewModelScope.launch {
            try {
                col.add(newReceipt.toMap()).await()
                sendEvent(UiEvent.Toast("Receipt added successfully"))
                sendEvent(UiPrivateEvent.NavigateBack)
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Error when adding receipt: ${e.message}"))
            }
        }
    }

    fun updateRecipe(updated: Recipe) {
        viewModelScope.launch {
            try {

                recipeDoc!!.update(
                    mapOf(
                        "receiptNumber" to updated.number,
                        "emittedDate" to updated.emittedAt,
                        "expireDate" to updated.expiresAt
                    )
                ).await()

                sendEvent(UiEvent.Toast("Recipe updated successfully"))
                sendEvent(UiPrivateEvent.NavigateBack)
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Error updating receipt: ${e.message}"))
            }
        }
    }


    fun deleteRecipe() {
        viewModelScope.launch {
            try {
                recipeDoc!!.delete().await()
                sendEvent(UiEvent.Toast("Recibo removido com sucesso"))
                sendEvent(UiPrivateEvent.NavigateBack)
            } catch (e: Exception) {
                Log.e("RecipeDetailsVM", "Erro ao remover recibo", e)
                sendEvent(UiEvent.Toast("Erro ao remover recibo: ${e.message}"))
            }
        }
    }


    fun administrateMedicine(medicine: RecipeMedicine) {
        viewModelScope.launch {
            try {

                val current = _recipeState.value.data
                val updatedMeds = current.medicines.map {
                    if (it.id == medicine.id && !it.isAdministrated)
                        it.copy(isAdministrated = true)
                    else it
                }
                val updatedRecipe = current.copy(medicines = updatedMeds)
                recipeDoc!!.set(updatedRecipe.toMap()).await()


                val dependentDoc = recipeDoc
                    .parent
                    .parent!!

                val medsCol = dependentDoc.collection("medicins")
                val snapshot = medsCol
                    .whereEqualTo("name", medicine.medicineName)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    val newMedData = mapOf(
                        "name" to medicine.medicineName,
                        "observations" to medicine.shortDescription,
                        "quantity" to medicine.quantity,

                        "dosePerPeriod" to 0,
                        "period" to 0,
                        "type" to ""
                    )
                    medsCol.add(newMedData).await()
                } else {
                    val doc = snapshot.documents.first()
                    val currentQty = (doc.getLong("quantity") ?: 0L).toInt()
                    doc.reference.update("quantity", currentQty + medicine.quantity).await()
                }


                _recipeState.value = _recipeState.value.copy(
                    data = updatedRecipe,
                    loading = false
                )
                sendEvent(UiEvent.Toast("Medication administered and stock updated successfully!"))
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Erro ao administrar medicamento: ${e.message}"))
            }
        }
    }

    fun addMedicine(med: RecipeMedicine) {
        viewModelScope.launch {
            try {

                val medWithId = if (med.id.isBlank()) {
                    med.copy(id = UUID.randomUUID().toString())
                } else med


                val current = _recipeState.value.data
                val updatedList = current.medicines + medWithId
                val updatedRecipe = current.copy(medicines = updatedList)


                recipeDoc!!.set(updatedRecipe.toMap()).await()


                _recipeState.value = _recipeState.value.copy(
                    data = updatedRecipe,
                    loading = false
                )
                sendEvent(UiEvent.Toast("Medicine Added"))
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Error adding medicine:: ${e.message}"))
            }
        }
    }

    fun editMedicine(updatedMed: RecipeMedicine) {
        viewModelScope.launch {
            try {

                val medWithId = if (updatedMed.id.isBlank()) {
                    updatedMed.copy(id = UUID.randomUUID().toString())
                } else updatedMed

                val current = _recipeState.value.data
                val updatedList = current.medicines.map {
                    if (it.id == medWithId.id) medWithId else it
                }
                val updatedRecipe = current.copy(medicines = updatedList)

                recipeDoc!!.set(updatedRecipe.toMap()).await()
                _recipeState.value = _recipeState.value.copy(
                    data = updatedRecipe,
                    loading = false
                )
                sendEvent(UiEvent.Toast("Medicine updated"))
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Error updating medication: ${e.message}"))
            }
        }
    }

    fun removeMedicine(medId: String) {
        viewModelScope.launch {
            try {
                val current = _recipeState.value.data
                val updatedList = current.medicines.filterNot { it.id == medId }
                val updatedRecipe = current.copy(medicines = updatedList)

                recipeDoc!!.set(updatedRecipe.toMap()).await()
                _recipeState.value = _recipeState.value.copy(
                    data = updatedRecipe,
                    loading = false
                )
                sendEvent(UiEvent.Toast("Medication removed"))
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Error when removing medication: ${e.message}"))
            }
        }
    }

    companion object {
        val RECEIPTS_REF = object : CreationExtras.Key<String> {}
        val RECIPE_ID = object : CreationExtras.Key<String?> {}

        val Factory = viewModelFactory {
            initializer {
                val ref = this[RECEIPTS_REF] ?: error("You must pass receiptsRef")
                val id = this[RECIPE_ID]
                RecipeDetailsViewModel(ref, id)
            }
        }
    }
}
