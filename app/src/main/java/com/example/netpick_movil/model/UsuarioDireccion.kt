package com.example.netpick_movil.model

data class UsuarioDireccion(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val clave: String,
    val telefono: String,
    val rol: Rol
)
