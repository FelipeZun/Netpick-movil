package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.model.Usuario

data class LoginUIState(
        val nombre: String = "",
        val telefono: String = "",
        val correo: String = "",
        val clave: String = "",
        val cargando: Boolean = false,
        val loginExitoso: Boolean = false,
        val registroExitoso: Boolean = false,
        val error: String? = null,
        val usuarioLogueado: Usuario? = null
    )

sealed interface LoginFormEvent {
        data class NombreChanged(val nombre: String) : LoginFormEvent
        data class TelefonoChanged(val telefono: String) : LoginFormEvent
        data class CorreoChanged(val correo: String) : LoginFormEvent
        data class ClaveChanged(val clave: String) : LoginFormEvent
        object Submit : LoginFormEvent
        object Register : LoginFormEvent
    }

sealed class RegisterFormEvent {
        data class NombreChanged(val value: String) : RegisterFormEvent()
        data class CorreoChanged(val value: String) : RegisterFormEvent()
        data class TelefonoChanged(val value: String) : RegisterFormEvent()
        data class ClaveChanged(val value: String) : RegisterFormEvent()
        data object Submit : RegisterFormEvent()
}

data class RegisterUiState(
        val nombre: String = "",
        val correo: String = "",
        val telefono: String = "",
        val clave: String = "",
        val cargando: Boolean = false,
        val error: String? = null,
        val registroExitoso: Boolean = false
)
