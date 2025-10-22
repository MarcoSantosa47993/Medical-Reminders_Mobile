package com.example.privateSystem.medicinsManagement.viewmodel

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
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.example.shared.utils.UiPrivateEvent
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class MedicinsListScreenViewModel(
    collectionRefStr: String,
) : ViewModel() {

    private val db: FirebaseFirestore = Firebase.firestore

    private val medicinsCollection = db.collection(collectionRefStr)

    private val _listState = MutableLiveData<BaseState>(
        BaseState.Init
    )
    val listState: LiveData<BaseState> = _listState

    var filteredList by mutableStateOf<List<Medicin>>(listOf())
    var searchValue by mutableStateOf("")

    init {
        liveMedicins()
    }

    fun getMedicins() {
        _listState.value = BaseState.Loading

        medicinsCollection.orderBy("name").get().addOnSuccessListener { query ->
            val medicins = query.map { Medicin.fromDocument(it) }

            _listState.value = BaseState.Success<List<Medicin>>(medicins)
            applyFilters()
        }.addOnFailureListener {
            _listState.value = BaseState.Error(it.message ?: "Error on get medicins list")
        }
    }

    fun liveMedicins() {
        medicinsCollection.orderBy("name").addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                _listState.value = BaseState.Error(error.message ?: "Error on listen medicins")
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                val medicins = querySnapshot.map { Medicin.fromDocument(it) }
                _listState.value = BaseState.Success<List<Medicin>>(medicins)
                applyFilters()
            }
        }
    }

    fun addMedicin(medicin: Medicin) {
        medicinsCollection.whereEqualTo("name", medicin.name).get()
            .addOnSuccessListener { docs ->
                if (docs.size() > 0) {
                    sendEvent(UiEvent.Toast("Medicin already exists!"))
                    return@addOnSuccessListener
                }

                val medicin = Medicin.empty.copy(
                    name = medicin.name,
                    type = medicin.type,
                    quantity = medicin.quantity,
                    dosePerPeriod = medicin.dosePerPeriod,
                    period = medicin.period,
                    observations = medicin.observations
                )

                medicinsCollection.add(medicin.toDocument()).addOnSuccessListener {
                    getMedicins()
                    sendEvent(UiEvent.Toast("New medicin was added"))
                    sendEvent(UiPrivateEvent.NavigateBack)
                }.addOnFailureListener { error ->
                    sendEvent(
                        UiEvent.Toast(
                            error.message ?: "Error on register medicin"
                        )
                    )
                }
            }
    }

    fun onSearch(value: String) {
        searchValue = value
        applyFilters()
    }

    fun applyFilters() {
        filteredList = if (_listState.value is BaseState.Success<*>) {
            if (searchValue.isEmpty()) {
                (_listState.value as BaseState.Success<List<Medicin>>).data
            } else {
                (_listState.value as BaseState.Success<List<Medicin>>).data.filter {
                    it.name.contains(searchValue, ignoreCase = true)
                }
            }
        } else {
            emptyList()
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
                MedicinsListScreenViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }
}