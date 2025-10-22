package com.example.privateSystem.receipsManagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.privateSystem.receipsManagement.models.Recipe
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReceipsViewModel(
    private val recipesRef: String
) : ViewModel() {

    private val db = Firebase.firestore
    private val recipesCollection = db.collection(recipesRef)

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        recipesCollection
            .get()
            .addOnSuccessListener { query ->
                recipes = query.map { Recipe.fromDocument(it) }
            }
            .addOnFailureListener { e ->
                recipes = emptyList()
            }
    }

    fun createReceipt(newReceipt: Recipe) {
        viewModelScope.launch {
            try {

                recipesCollection.add(newReceipt.toMap()).await()

                sendEvent(UiEvent.Toast("Receipt criada com sucesso"))

                sendEvent(UiPrivateEvent.NavigateBack)

                fetchRecipes()
            } catch (e: Exception) {
                sendEvent(UiEvent.Toast("Erro ao criar receipt: ${e.message}"))
            }
        }
    }

    companion object {
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val ref = this[K_COLLECTION_REF]
                    ?: error("É obrigatório passar recipesRef")
                ReceipsViewModel(recipesRef = ref)
            }
        }
    }
}
