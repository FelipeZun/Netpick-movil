package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.data.repository.AuthRepository
import com.example.netpick_movil.data.repository.SessionManager
import com.example.netpick_movil.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.NombreChanged ->
                _uiState.update { it.copy(nombre = event.value) }

            is RegisterFormEvent.CorreoChanged ->
                _uiState.update { it.copy(correo = event.value) }

            is RegisterFormEvent.TelefonoChanged ->
                _uiState.update { it.copy(telefono = event.value) }

            is RegisterFormEvent.ClaveChanged ->
                _uiState.update { it.copy(clave = event.value) }

            RegisterFormEvent.Submit -> registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }

            try {
                val response = authApi.register(
                    RegisterRequest(
                        nombre = state.nombre,
                        correo = state.correo,
                        clave = state.clave,
                        telefono = state.telefono
                    )
                )

                if (response.isSuccessful) {
                    _uiState.update { it.copy(registroExitoso = true, cargando = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Error del servidor: ${response.message()}",
                            cargando = false
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, cargando = false) }
            }
        }
    }
}