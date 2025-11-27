package com.example.netpick_movil.model

data class LoginResponse(
    val message: String,
    val userId: Int,
    val nombre: String,
    val rol: String
)
