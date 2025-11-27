package com.example.netpick_movil.model

data class RegisterRequest(
    val nombre: String,
    val correo: String,
    val clave: String,
    val telefono: String
)
