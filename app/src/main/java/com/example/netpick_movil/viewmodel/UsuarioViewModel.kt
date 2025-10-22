package com.example.netpick_movil.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.UsuarioErrores
import com.example.netpick_movil.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUIState())
    val uiState: StateFlow<UsuarioUIState> = _uiState.asStateFlow()

    fun onEvent(event: UsuarioFormEvent) {
        when (event) {
            is UsuarioFormEvent.NombreChanged -> {
                _uiState.update { it.copy(nombre = event.nombre) }
            }
            is UsuarioFormEvent.CorreoChanged -> {
                _uiState.update { it.copy(correo = event.correo) }
            }
            is UsuarioFormEvent.ClaveChanged -> {
                _uiState.update { it.copy(clave = event.clave) }
            }
            is UsuarioFormEvent.DireccionChanged -> {
                _uiState.update { it.copy(direccion = event.direccion) }
            }
            is UsuarioFormEvent.AceptaTerminosChanged -> {
                _uiState.update { it.copy(aceptaTerminos = event.acepta) }
            }
            is UsuarioFormEvent.Submit -> {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val errores = UsuarioErrores(
            nombre = if (state.nombre.isBlank()) "El nombre no puede estar vacío" else null,
            correo = if (!Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) "El correo no es válido" else null,
            clave = if (state.clave.length < 6) "La clave debe tener al menos 6 caracteres" else null,
            aceptaTerminos = if (!state.aceptaTerminos) "Debes aceptar los términos y condiciones" else null
        )

        _uiState.update { it.copy(errores = errores) }

        val hasError = listOf(errores.nombre, errores.correo, errores.clave, errores.aceptaTerminos).any { it != null }

        if (!hasError) {
            // TODO: Handle successful registration
        }
    }
}

sealed interface UsuarioFormEvent {
    data class NombreChanged(val nombre: String) : UsuarioFormEvent
    data class CorreoChanged(val correo: String) : UsuarioFormEvent
    data class ClaveChanged(val clave: String) : UsuarioFormEvent
    data class DireccionChanged(val direccion: String) : UsuarioFormEvent
    data class AceptaTerminosChanged(val acepta: Boolean) : UsuarioFormEvent
    object Submit : UsuarioFormEvent
}
