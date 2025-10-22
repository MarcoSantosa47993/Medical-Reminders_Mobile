package com.example.privateSystem.medicinsManagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shared.BaseState
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class MedicinsDetailsScreenViewModel(
    collectionRefStr: String,
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    private val medicinsCollection = db.document(collectionRefStr)

    private val _state = MutableLiveData<BaseState>(BaseState.Init)
    val state: LiveData<BaseState> = _state

    init {
        getMedicin()
    }

    fun getMedicin() {
        _state.value = BaseState.Loading

        medicinsCollection.get()
            .addOnSuccessListener {
                val medicin = Medicin.fromDocument(it)
                _state.value = BaseState.Success<Medicin>(medicin)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on get medicin")
            }
    }

    fun setMedicin(data: Medicin) {
        _state.value = BaseState.Loading

        medicinsCollection.set(data.toDocument())
            .addOnSuccessListener {
                sendEvent(UiEvent.Toast("Medicin was updated"))
                _state.value = BaseState.Success(data)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on update medicin")
            }
    }

    fun removeMedicin() {
        _state.value = BaseState.Loading

        medicinsCollection.delete()
            .addOnSuccessListener {
                sendEvent(UiEvent.Toast("Medicin was deleted"))
                sendEvent(UiPrivateEvent.NavigateBack)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on delete medicin")
            }
    }

    suspend fun getDependentName(medicinsRef: String): String? {
        val firestore = FirebaseFirestore.getInstance()
        val dependentRefPath = medicinsRef.substringBefore("/medicins")

        return try {
            val doc = firestore.document(dependentRefPath).get().await()
            if (doc.exists()) {
                doc.getString("name")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val collectionRefStr = this[K_COLLECTION_REF] as String
                MedicinsDetailsScreenViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }
}