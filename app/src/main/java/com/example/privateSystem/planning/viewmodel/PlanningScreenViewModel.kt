package com.example.privateSystem.planning.viewmodel

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
import com.example.privateSystem.medicinsManagement.models.Medicin
import com.example.privateSystem.planning.utils.getPeriodRangeByDate
import com.example.shared.BaseState
import com.example.shared.models.Plan
import com.example.shared.sendEvent
import com.example.shared.utils.UiEvent
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class PlanningScreenViewModel(
    collectionRefStr: String,
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val planningCollection = db.collection(collectionRefStr)
    private val medicinesCollection =
        db.collection(collectionRefStr.replace("/planning", "/medicins"))

    var selectedDate by mutableStateOf<LocalDate>(LocalDate.now())

    private val _listState = MutableLiveData<BaseState>(BaseState.Init)
    val listState: LiveData<BaseState> = _listState

    var medicines by mutableStateOf<List<Medicin>>(emptyList())

    var isLoadingForm by mutableStateOf<Boolean>(false)

    init {
        getPlans(selectedDate)
        getMedicines()
    }


    fun getMedicines() {
        medicinesCollection.orderBy("name")
            .get()
            .addOnSuccessListener { query ->
                val m = query.map { Medicin.fromDocument(it) }
                medicines = m
            }
            .addOnFailureListener {
                medicines = emptyList()
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getPlans(date: LocalDate) {
        _listState.value = BaseState.Loading

        val zoneID = ZoneId.systemDefault()

        val firstDate = date.atTime(LocalTime.MIN).atZone(zoneID).plusHours(1).toInstant()
        val lastDate = date.atTime(LocalTime.MAX).atZone(zoneID).plusHours(1).toInstant()

        Log.e("firstDate", firstDate.toString())
        Log.e("secondDate", lastDate.toString())


        planningCollection
            .whereGreaterThanOrEqualTo(
                "takeAt",
                Timestamp(time = firstDate)
            )
            .whereLessThanOrEqualTo("takeAt", Timestamp(time = lastDate))
            .orderBy("takeAt")
            .get()
            .addOnSuccessListener { docs ->
                Log.e("TESTEEEee", docs.size().toString())
                val plans = docs.map { Plan.fromDocument(it) }
                _listState.value = BaseState.Success<List<Plan>>(plans)
            }
            .addOnFailureListener {
                _listState.value = BaseState.Error(it.message ?: "Error on get plans")
            }
    }

    fun onChangeSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
        getPlans(newDate)
    }

    fun removePlan(planId: String, medicineId: String) {
        if (planId.isNotBlank()) {
            isLoadingForm = true
            planningCollection.document(planId).delete()
                .addOnSuccessListener {
                    getPlans(selectedDate)
                    updateStock(medicineId, 1)
                    isLoadingForm = false
                }
                .addOnFailureListener {
                    sendEvent(UiEvent.Toast(it.message ?: "Error on remove plan"))
                    isLoadingForm = false
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun setPlan(plan: Plan) {
        isLoadingForm = true

        medicinesCollection.document(plan.medicineId).get()
            .addOnSuccessListener { docMedicine ->
                val medicine = Medicin.fromDocument(docMedicine)

                val zoneId = ZoneId.systemDefault()
                val instant = plan.takeAt.toInstant()
                val dateRange =
                    getPeriodRangeByDate(LocalDate.ofInstant(instant, zoneId), medicine.period)

                planningCollection
                    .whereEqualTo("medicineId", plan.medicineId)
                    .whereGreaterThanOrEqualTo("takeAt", Timestamp(time = dateRange.first))
                    .whereLessThanOrEqualTo("takeAt", Timestamp(time = dateRange.second))
                    .get()
                    .addOnSuccessListener { query ->
                        if (query.size() >= medicine.dosePerPeriod) {
                            sendEvent(UiEvent.Toast("Dependent can't take more doses"))
                            isLoadingForm = false
                        } else {
                            if (plan.id.isEmpty()) {
                                planningCollection.add(plan.toDocument())
                                    .addOnSuccessListener {
                                        getPlans(selectedDate)
                                        updateStock(plan.medicineId)
                                        isLoadingForm = false
                                    }
                                    .addOnFailureListener {
                                        sendEvent(
                                            UiEvent.Toast(
                                                it.message ?: "Error on create plan"
                                            )
                                        )
                                        isLoadingForm = false
                                    }
                            } else {
                                // update plan
                                planningCollection.document(plan.id)
                                    .set(plan.toDocument())
                                    .addOnSuccessListener {
                                        getPlans(selectedDate)
                                        isLoadingForm = false
                                    }
                                    .addOnFailureListener {
                                        sendEvent(
                                            UiEvent.Toast(
                                                it.message ?: "Error on update plan"
                                            )
                                        )
                                        isLoadingForm = false
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        sendEvent(UiEvent.Toast(it.message ?: "Error on check doses"))
                        isLoadingForm = false
                    }
            }
            .addOnFailureListener {
                sendEvent(UiEvent.Toast(it.message ?: "Error on get medicine"))
                isLoadingForm = false
            }


    }

    fun updateStock(medicineId: String, value: Int = -1){
        medicinesCollection.document(medicineId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()){
                    val medicine = Medicin.fromDocument(doc)
                    medicine.quantity = medicine.quantity + value

                    medicinesCollection.document(medicineId)
                        .set(medicine.toDocument())
                        .addOnSuccessListener {
                            getMedicines()
                        }
                        .addOnFailureListener {
                            sendEvent(UiEvent.Toast("error on update quantity"))
                        }

                }else{
                    sendEvent(UiEvent.Toast("medicine not found"))
                }

            }
            .addOnFailureListener {
                sendEvent(UiEvent.Toast("error on get medicine"))
            }
    }

    companion object {
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val collectionRefStr = this[K_COLLECTION_REF] as String
                PlanningScreenViewModel(
                    collectionRefStr = collectionRefStr,
                )
            }
        }
    }
}