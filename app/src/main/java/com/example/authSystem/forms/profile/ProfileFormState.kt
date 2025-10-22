package com.example.authSystem.forms.profile

import android.graphics.Bitmap
import com.example.shared.enums.MyUserRoles

data class ProfileFormState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val birthday: Long? = null,
    val birthdayError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val phone2: String = "",
    val phone2Error: String? = null,
    val location: String = "",
    val locationError: String? = null,
    val role: Int = MyUserRoles.dependent.ordinal,
    val roleError: String? = null,
    val image: Bitmap? = null,
    val imageError: String? = null,
)