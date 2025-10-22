package com.example.privateSystem.dashboard.viewmodel

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
import com.example.shared.BaseState
import com.example.shared.enums.MyUserRoles
import com.example.shared.enums.PlanStatus
import com.example.shared.models.Dependent
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
class DashboardViewModel(
    userDocumentRefStr: String,
    userRole: MyUserRoles,
    userId: String
) : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    private val dependentsCollection = db.collection("$userDocumentRefStr/dependents")


    var selectedDependent by mutableStateOf<String>("")
    fun onChangeSelectedDependent(newDependent: String) {
        selectedDependent = newDependent
        getPlanAlerts()
        getMedicines()
        getDayPlan(selectedDate)
    }


    private val _listDependents = MutableLiveData<BaseState>(BaseState.Init)
    val listDependents: LiveData<BaseState> = _listDependents

    private val _medicines = MutableLiveData<Map<String, String>>(emptyMap())
    val medicines: LiveData<Map<String, String>> = _medicines

    private val _listAlerts = MutableLiveData<BaseState>(BaseState.Init)
    val listAlerts: LiveData<BaseState> = _listAlerts

    private val _listPlans = MutableLiveData<BaseState>(BaseState.Init)
    val listPlans: LiveData<BaseState> = _listPlans

    var selectedDate by mutableStateOf<LocalDate>(LocalDate.now())
    fun onChangeSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
        getDayPlan(newDate)
    }

    fun load(userRole: MyUserRoles, userId: String){
        Log.i("entrou", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        if (userRole == MyUserRoles.caregiver){
            getDependents()
        }else{

            getMyDependent(userId)
        }
        getPlanAlerts()
        getMedicines()

        selectedDate = LocalDate.now()
        getDayPlan(selectedDate)
    }

    fun getMyDependent(userId: String){
        _listDependents.value = BaseState.Loading
        val collectionGroup = db.collectionGroup("dependents")

        collectionGroup.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { query ->
                if (query.size() > 0){
                    val document = query.single()

                    val id = document.id
                    Log.i("id", id)

                    val dependent = Dependent.fromDocument(document)
                    Log.i("dependent", dependent.toString())
                    _listDependents.value = BaseState.Success<List<Dependent>>(emptyList())
                    onChangeSelectedDependent(document.id)
                }
            }
            .addOnFailureListener {
                _listDependents.value =
                    BaseState.Error(it.message ?: "Error on get dependents list")
            }
    }

    fun getMedicines() {
        if (selectedDependent.isBlank()) {
            _medicines.value = emptyMap()
            return
        }

        val medicinesCollection = dependentsCollection
            .document(selectedDependent).collection("medicins")

        medicinesCollection.get()
            .addOnSuccessListener { query ->
                val m = query.map { Medicin.fromDocument(it) }
                    .associate { it.id to it.name }
                Log.i("EEEEEEEee", m.toString())
                _medicines.value = m
            }
            .addOnFailureListener {
                _medicines.value = emptyMap()
                sendEvent(UiEvent.Toast(it.message ?: "Error on get dependent medicines"))
            }

    }

    fun changePlanStatus(plan: Plan, status: PlanStatus) {
        if (selectedDependent.isNotBlank()) {
            val p = plan.copy(taked = status)

            val planningCollection = dependentsCollection
                .document(selectedDependent).collection("planning")

            planningCollection.document(plan.id)
                .set(p.toDocument())
                .addOnSuccessListener {
                    getPlanAlerts()
                    getDayPlan(selectedDate)
                }
                .addOnFailureListener {
                    sendEvent(UiEvent.Toast(it.message ?: "Error on change plan status"))
                }
        }
    }

    fun getDayPlan(date: LocalDate) {
        _listPlans.value = BaseState.Loading

        if (selectedDependent.isNotBlank()) {
            val zoneID = ZoneId.systemDefault()

            val firstDate = date.atTime(LocalTime.MIN).atZone(zoneID).plusHours(1).toInstant()
            val lastDate = date.atTime(LocalTime.MAX).atZone(zoneID).plusHours(1).toInstant()

            val planningCollection = dependentsCollection
                .document(selectedDependent).collection("planning")

            planningCollection
                .whereGreaterThanOrEqualTo(
                    "takeAt",
                    Timestamp(time = firstDate)
                )
                .whereLessThanOrEqualTo("takeAt", Timestamp(time = lastDate))
                .orderBy("takeAt")
                .get()
                .addOnSuccessListener { docs ->
                    val plans = docs.map { Plan.fromDocument(it) }
                    _listPlans.value = BaseState.Success<List<Plan>>(plans)
                }
                .addOnFailureListener {
                    _listPlans.value = BaseState.Error(it.message ?: "Error on get plans")
                }
        }


    }

    fun getPlanAlerts() {
        _listAlerts.value = BaseState.Loading
        if (selectedDependent.isBlank()) {
            _listAlerts.value = BaseState.Success<List<Plan>>(emptyList())
            return
        }

        val planningCollection = dependentsCollection
            .document(selectedDependent).collection("planning")


        val timeStamp = Timestamp.now()
        planningCollection
            .whereEqualTo("taked", PlanStatus.PENDING.ordinal)
            .whereLessThanOrEqualTo("takeAt", timeStamp)
            .orderBy("takeAt")
            .get()
            .addOnSuccessListener { query ->
                val plans = query.map { Plan.fromDocument(it) }
                _listAlerts.value = BaseState.Success<List<Plan>>(plans)
            }
            .addOnFailureListener {
                _listAlerts.value = BaseState.Error(it.message ?: "Error on get dependent alerts")
                sendEvent(UiEvent.Toast(it.message ?: "Error on get dependent alerts"))
            }
    }


    fun getDependents() {
        _listDependents.value = BaseState.Loading

        dependentsCollection
            .orderBy("name")
            .get()
            .addOnSuccessListener { query ->
                val dependents = query.map { Dependent.fromDocument(it) }
                if (dependents.isNotEmpty()) {
                    onChangeSelectedDependent(dependents.first().id)
                }
                Log.i("dependents", dependents.toString())
                _listDependents.value = BaseState.Success(dependents)
            }
            .addOnFailureListener {
                _listDependents.value =
                    BaseState.Error(it.message ?: "Error on get dependents list")
            }
    }


    companion object {
        val K_COLLECTION_REF = object : CreationExtras.Key<String> {}
        val K_USER_ROLE = object : CreationExtras.Key<MyUserRoles> {}
        val K_USER_ID = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userDocumentRefStr = this[K_COLLECTION_REF] as String
                val userRole = this[K_USER_ROLE] as MyUserRoles
                val userId = this[K_USER_ID] as String

                DashboardViewModel(
                    userDocumentRefStr = userDocumentRefStr,
                    userRole = userRole,
                    userId = userId
                )
            }
        }
    }
}