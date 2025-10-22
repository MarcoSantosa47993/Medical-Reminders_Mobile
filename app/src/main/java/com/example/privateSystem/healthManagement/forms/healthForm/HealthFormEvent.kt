package com.example.privateSystem.healthManagement.forms.healthForm

import android.graphics.Bitmap

sealed class HealthFormEvent {
    data class LabelChanged(val value: String) : HealthFormEvent()
    data class ValueChanged(val value: String) : HealthFormEvent()
    data class UnitChanged(val value: String) : HealthFormEvent()
    data class ImageChanged(val value: Bitmap?) : HealthFormEvent()

    object Submit : HealthFormEvent()
}