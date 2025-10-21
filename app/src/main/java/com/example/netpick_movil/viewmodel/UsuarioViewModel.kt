package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
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
                // TODO: Handle form submission
            }
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
