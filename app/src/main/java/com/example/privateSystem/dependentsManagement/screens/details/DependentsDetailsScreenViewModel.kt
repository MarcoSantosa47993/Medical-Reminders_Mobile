package com.example.privateSystem.dependentsManagement.screens.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shared.BaseState
import com.example.shared.models.Dependent
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class DependentsDetailsScreenViewModel(
    collectionRefStr: String,
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    private val docRef = db.document(collectionRefStr)

    private val _state = MutableLiveData<BaseState>(BaseState.Init)
    val state: LiveData<BaseState> = _state

    init {
        getDependent()
    }

    fun getDependent() {
        _state.value = BaseState.Loading

        docRef.get()
            .addOnSuccessListener {
                val dependent = Dependent.fromDocument(it)
                _state.value = BaseState.Success<Dependent>(dependent)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on get dependent")
            }
    }

    fun setDependent(data: Dependent) {
        _state.value = BaseState.Loading

        docRef.set(data.toDocument())
            .addOnSuccessListener {
                _state.value = BaseState.Success(data)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on update dependent")
            }
    }

    fun removeDependent() {
        _state.value = BaseState.Loading

        docRef.delete()
            .addOnSuccessListener {
                sendEvent(UiEvent.Toast("Dependent was deleted"))
                sendEvent(UiPrivateEvent.NavigateBack)
            }
            .addOnFailureListener {
                _state.value = BaseState.Error(it.message ?: "Error on delete dependent")
            }
    }

    companion object {
        // Define a custom key for your dependency
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val collectionRefStr = this[K_COLLECTION_REF] as String
                DependentsDetailsScreenViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }
}