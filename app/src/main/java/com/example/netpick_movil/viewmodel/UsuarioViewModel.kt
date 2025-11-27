package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.RegisterRequest
import com.example.netpick_movil.model.Usuario
import com.example.netpick_movil.model.UsuarioErrores
import com.example.netpick_movil.model.UsuarioUIState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class UsuarioViewModel(private val dao: UsuarioDao) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUIState())
    val uiState: StateFlow<UsuarioUIState> = _uiState.asStateFlow()
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    fun onEvent(event: UsuarioFormEvent) {
        when (event) {
            is UsuarioFormEvent.NombreChanged -> _uiState.update { it.copy(nombre = event.nombre) }
            is UsuarioFormEvent.CorreoChanged -> _uiState.update { it.copy(correo = event.correo) }
            is UsuarioFormEvent.ClaveChanged -> _uiState.update { it.copy(clave = event.clave) }
            is UsuarioFormEvent.TelefonoChanged -> _uiState.update { it.copy(telefono = event.telefono) } // CORREGIDO: Manejo del teléfono
            is UsuarioFormEvent.DireccionChanged -> _uiState.update { it.copy(direccion = event.direccion) }
            is UsuarioFormEvent.AceptaTerminosChanged -> _uiState.update { it.copy(aceptaTerminos = event.acepta) }
            is UsuarioFormEvent.Submit -> validateForm()
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        val phoneRegex = "^[0-9]{8,15}$".toRegex()

        val errores = UsuarioErrores(
            nombre = if (state.nombre.isBlank()) "El nombre no puede estar vacío" else null,
            correo = if (!emailRegex.matches(state.correo)) "El correo no es válido" else null,
            clave = if (state.clave.length < 6) "La clave debe tener al menos 6 caracteres" else null,
            telefono = if (!phoneRegex.matches(state.telefono)) "El teléfono debe contener solo números (min 8)" else null, // AÑADIDO: Validación de teléfono
            direccion = if (state.direccion.isBlank()) "La dirección no puede estar vacía" else null, // Nota: Dirección se hace opcional en el Canvas, pero el ViewModel la hace obligatoria.
            aceptaTerminos = if (!state.aceptaTerminos) "Debes aceptar términos" else null
        )

        _uiState.update { it.copy(errores = errores) }

        val hasError = listOf(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.telefono,
            errores.direccion,
            errores.aceptaTerminos
        ).any { it != null }

        if (!hasError) {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    nombre = _uiState.value.nombre,
                    correo = _uiState.value.correo,
                    clave = _uiState.value.clave,
                    telefono = _uiState.value.telefono
                )

                val response = RetrofitClient.authApi.register(request)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(errores = UsuarioErrores(registro = "¡Cuenta creada con éxito!"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = if (errorBody.isNullOrBlank()) {
                        "Error ${response.code()}: Respuesta vacía o inesperada."
                    } else {
                        "Error ${response.code()}: $errorBody"
                    }

                    _uiState.update {
                        it.copy(errores = UsuarioErrores(registro = errorMsg))
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errores = UsuarioErrores(registro = "Error de conexión/parseo: ${e.message}"))
                }
            }
        }
    }

    fun cargarUsuarioPorCorreo(correo: String) {
        viewModelScope.launch {
            try {
                val user = dao.buscarPorCorreo(correo)
                _usuario.value = user
            } catch (e: Exception) {
                println("Error al cargar usuario: ${e.message}")
            }
        }
    }
}

sealed interface UsuarioFormEvent {
    data class NombreChanged(val nombre: String) : UsuarioFormEvent
    data class CorreoChanged(val correo: String) : UsuarioFormEvent
    data class ClaveChanged(val clave: String) : UsuarioFormEvent
    data class DireccionChanged(val direccion: String) : UsuarioFormEvent
    data class TelefonoChanged(val telefono: String) : UsuarioFormEvent
    data class AceptaTerminosChanged(val acepta: Boolean) : UsuarioFormEvent
    object Submit : UsuarioFormEvent
}