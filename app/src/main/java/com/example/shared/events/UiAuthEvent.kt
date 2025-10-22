package com.example.shared.events

import com.example.shared.models.MyUser

sealed interface UiAuthEvent {
    data class Register(val myUser: MyUser, val password: String) : UiAuthEvent
    data class Login(val email: String, val password: String) : UiAuthEvent
}