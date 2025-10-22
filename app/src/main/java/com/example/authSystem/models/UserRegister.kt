package com.example.authSystem.models

data class UserRegisterModel(
    val name: String,
    val email: String,
    val birthday: String,
    val password: String,
    val confPassword: String
)