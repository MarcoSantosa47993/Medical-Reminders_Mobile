package com.example.privateSystem.receipsManagement.models

data class RecipeMedicine(
    val id: String,
    val medicineName: String,
    val shortDescription: String,
    val quantity: Int,
    val isAdministrated: Boolean = false
) {
    companion object {
        val empty = RecipeMedicine(
            id = "",
            medicineName = "",
            shortDescription = "",
            quantity = 0,
            isAdministrated = false
        )

    
        fun <T> isValid(field: String, value: T): String? {
            when (field) {
                "medicineName" -> {
                    if ((value as String).isEmpty()) {
                        return "Medicine name is required"
                    }
                }

                "quantity" -> {
                    if ((value as Int) < 0) {
                        return "Quantity can't be less than 0"
                    }
                }
            }

            return null
        }
    }

    fun isValid(): Boolean {
        return RecipeMedicine.isValid("medicineName", medicineName).isNullOrEmpty() &&
                RecipeMedicine.isValid("quantity", quantity).isNullOrEmpty()
    }
}