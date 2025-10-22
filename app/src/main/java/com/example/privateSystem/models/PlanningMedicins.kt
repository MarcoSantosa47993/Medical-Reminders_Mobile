package com.example.privateSystem.models

import com.example.privateSystem.planning.models.MedicineStatus

data class PlanningMedicins(
    val name: String,
    val time: String,
    val status: MedicineStatus,
    val fast: Boolean
)

val planningMedicins = listOf(
    PlanningMedicins(
        name = "Cozzar",
        time = "07h00",
        fast = false,
        status = MedicineStatus.NOT_DONE
    ),
    PlanningMedicins(
        name = "Diatime",
        time = "08h00",
        fast = true,
        status = MedicineStatus.DONE
    ),
    PlanningMedicins(
        name = "Zarator",
        time = "08h30",
        fast = false,
        status = MedicineStatus.DONE
    ),
    PlanningMedicins(
        name = "Omeprazol",
        time = "12h00",
        fast = true,
        status = MedicineStatus.NOT_TAKEN
    ),
    PlanningMedicins(
        name = "Aspirina",
        time = "20h00",
        fast = false,
        status = MedicineStatus.NOT_TAKEN
    ),
    PlanningMedicins(
        name = "Victan",
        time = "22h30",
        fast = false,
        status = MedicineStatus.NOT_DONE
    ),
)