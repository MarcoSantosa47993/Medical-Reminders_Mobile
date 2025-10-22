package com.example.privateSystem.dependentsManagement.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shared.BaseState
import com.example.shared.enums.MyUserRoles
import com.example.shared.models.Dependent
import com.example.shared.models.MyUser
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class DependentsListScreenViewModel(
    collectionRefStr: String,
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    private val dependentsCollection = db.collection(collectionRefStr)

    private val _listState = MutableLiveData<BaseState>(
        BaseState.Init
    )
    val listState: LiveData<BaseState> = _listState

    var filteredList by mutableStateOf<List<Dependent>>(listOf())
    var searchValue by mutableStateOf("")


    init {
        getDependents()
    }

    fun getDependents() {
        _listState.value = BaseState.Loading

        dependentsCollection.orderBy("name").get().addOnSuccessListener { query ->
            val dependents = query.map { Dependent.fromDocument(it) }
            _listState.value = BaseState.Success<List<Dependent>>(dependents)
            applyFilters()
        }.addOnFailureListener {
            _listState.value = BaseState.Error(it.message ?: "Error on get dependents list")
        }
    }

    fun addDependent(pinCode: Int) {
        val usersCollection = db.collection("users")

        usersCollection.whereEqualTo("pinCode", pinCode).get()
            .addOnSuccessListener { documents ->
                if (documents.size() == 0) {
                    sendEvent(UiEvent.Toast("No dependents found!"))
                    return@addOnSuccessListener
                }

                val user = MyUser.fromDocument(documents.single())

                if (user.role == MyUserRoles.caregiver) {
                    sendEvent(UiEvent.Toast("This user is not a dependent!"))
                    return@addOnSuccessListener
                }

                // verify if dependent already exists
                dependentsCollection.whereEqualTo("userId", user.id).get()
                    .addOnSuccessListener { docs ->
                        if (docs.size() > 0) {
                            sendEvent(UiEvent.Toast("Dependent already exists!"))
                            return@addOnSuccessListener
                        }

                        val dependent = Dependent.empty.copy(
                            phone = user.phone,
                            phone2 = user.phone2,
                            userId = user.id,
                            birthday = user.birthday,
                            location = user.location,
                            name = user.name
                        )

                        dependentsCollection.add(dependent.toDocument()).addOnSuccessListener {
                            sendEvent(UiEvent.Toast("New dependent was added"))
                            getDependents()
                        }.addOnFailureListener { error ->
                            sendEvent(
                                UiEvent.Toast(
                                    error.message ?: "Error on register dependent"
                                )
                            )
                        }
                    }.addOnFailureListener { error ->
                        sendEvent(
                            UiEvent.Toast(
                                error.message ?: "Error on check if dependent already exists"
                            )
                        )
                    }

            }.addOnFailureListener { error ->
                sendEvent(UiEvent.Toast(error.message ?: "Error on get dependent by pincode"))
            }
    }

    fun onSearch(value: String) {
        searchValue = value
        applyFilters()
    }

    fun applyFilters() {
        filteredList = if (_listState.value is BaseState.Success<*>) {
            if (searchValue.isEmpty()) {
                (_listState.value as BaseState.Success<List<Dependent>>).data
            } else {
                (_listState.value as BaseState.Success<List<Dependent>>).data.filter {
                    it.name.contains(searchValue, ignoreCase = true)
                }
            }
        } else {
            emptyList()
        }
    }


    companion object {
        // Define a custom key for your dependency
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val collectionRefStr = this[K_COLLECTION_REF] as String
                DependentsListScreenViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }
}