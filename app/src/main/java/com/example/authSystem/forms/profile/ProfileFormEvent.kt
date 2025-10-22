package com.example.authSystem.forms.profile

import android.graphics.Bitmap

sealed class ProfileFormEvent {
    data class ImageChanged(val value: Bitmap?) : ProfileFormEvent()
    data class NameChanged(val value: String) : ProfileFormEvent()
    data class EmailChanged(val value: String) : ProfileFormEvent()
    data class BirthdayChanged(val value: Long?) : ProfileFormEvent()
    data class RoleChanged(val value: Int) : ProfileFormEvent()
    data class PhoneChanged(val value: String) : ProfileFormEvent()
    data class Phone2Changed(val value: String) : ProfileFormEvent()
    data class LocationChanged(val value: String) : ProfileFormEvent()

    object Submit : ProfileFormEvent()
}