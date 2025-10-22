package com.example.privateSystem.healthManagement.screens.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.shared.models.Health
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class HistoryViewModel(
    collectionRefStr: String,
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val healthCollection = db.collection(collectionRefStr)

    var selectedDate by mutableStateOf<LocalDate>(LocalDate.now())
    fun onChangeSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
        getHealthList(newDate)
    }

    private val _listState = MutableLiveData<BaseState>(BaseState.Init)
    val listState: LiveData<BaseState> = _listState

    var isLoadingForm by mutableStateOf<Boolean>(false)

    init {
        getHealthList(selectedDate)
    }


    fun getHealthList(date: LocalDate) {
        _listState.value = BaseState.Loading

        val zoneID = ZoneId.systemDefault()
        val firstDate = date.atTime(LocalTime.MIN).atZone(zoneID).toInstant()
        val lastDate = date.atTime(LocalTime.MAX).atZone(zoneID).toInstant()


        healthCollection
            .whereGreaterThanOrEqualTo(
                "date",
                Timestamp(time = firstDate)
            )
            .whereLessThanOrEqualTo("date", Timestamp(time = lastDate))
            .orderBy("label")
            .get()
            .addOnSuccessListener { docs ->
                try {
                    val l = docs.map { Health.fromDocument(it) }
                    _listState.value = BaseState.Success<List<Health>>(l)
                } catch (e: Exception) {
                    Log.e("EROORRORORORO", e.message ?: "aslkdaslkdjaslkdj")
                    _listState.value =
                        BaseState.Error(e.message ?: "Error on get health list items")
                }

            }
            .addOnFailureListener {
                _listState.value = BaseState.Error(it.message ?: "Error on get health list items")
            }
    }

    fun removeHealth(healthId: String) {
        if (healthId.isNotBlank()) {
            isLoadingForm = true
            healthCollection.document(healthId).delete()
                .addOnSuccessListener {
                    getHealthList(selectedDate)
                    isLoadingForm = false
                }
                .addOnFailureListener {
                    sendEvent(UiEvent.Toast(it.message ?: "Error on remove health item"))
                    isLoadingForm = false
                }
        }
    }

    fun setHealth(health: Health) {
        isLoadingForm = true

        if (health.id.isEmpty()) {
            healthCollection.add(health.toDocument())
                .addOnSuccessListener {
                    getHealthList(selectedDate)
                    isLoadingForm = false
                }
                .addOnFailureListener {
                    sendEvent(
                        UiEvent.Toast(
                            it.message ?: "Error on create health item"
                        )
                    )
                    isLoadingForm = false
                }
        } else {
            // update health
            healthCollection.document(health.id)
                .set(health.toDocument())
                .addOnSuccessListener {
                    getHealthList(selectedDate)
                    isLoadingForm = false
                }
                .addOnFailureListener {
                    sendEvent(
                        UiEvent.Toast(
                            it.message ?: "Error on update health item"
                        )
                    )
                    isLoadingForm = false
                }
        }
    }

    companion object {
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val collectionRefStr = this[K_COLLECTION_REF] as String
                HistoryViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }

}