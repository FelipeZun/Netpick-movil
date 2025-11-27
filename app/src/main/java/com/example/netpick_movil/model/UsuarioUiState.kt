package com.example.netpick_movil.model

import com.example.netpick_movil.model.UsuarioErrores

data class UsuarioUIState (
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores(),
    val registroExitoso: Boolean = false
)
