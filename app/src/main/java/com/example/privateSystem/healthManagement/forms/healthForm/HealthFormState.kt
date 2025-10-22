package com.example.privateSystem.healthManagement.forms.healthForm

import android.graphics.Bitmap

data class HealthFormState(
    val label: String = "",
    val labelError: String? = null,
    val value: String = "",
    val valueError: String? = null,
    val unit: String = "",
    val unitError: String? = null,
    val image: Bitmap? = null,
    val imageError: String? = null,
)