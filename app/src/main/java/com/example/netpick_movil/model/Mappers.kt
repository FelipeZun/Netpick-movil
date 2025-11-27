package com.example.netpick_movil.model

fun UserSession.toUsuario(): Usuario {
    return Usuario(
        id = this.userId,
        nombre = this.nombre,
        correo = this.correo,
        rol = this.rol,
        clave = "",
        direccion = "",
        telefono = ""
    )
}