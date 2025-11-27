package com.example.netpick_movil.model

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false
)
