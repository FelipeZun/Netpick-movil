package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.repository.AuthRepository
import com.example.netpick_movil.data.repository.Result
import com.example.netpick_movil.model.RegisterRequest
import com.example.netpick_movil.model.toUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    init {
        repository.getLoggedInUser()?.let { savedUser ->
            _uiState.update { state ->
                state.copy(
                    usuarioLogueado = savedUser.toUsuario(),
                    loginExitoso = true
                )
            }
        }
    }

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.CorreoChanged -> _uiState.update { it.copy(correo = event.correo, error = null) }
            is LoginFormEvent.ClaveChanged -> _uiState.update { it.copy(clave = event.clave, error = null) }
            is LoginFormEvent.NombreChanged -> _uiState.update { it.copy(nombre = event.nombre, error = null) }
            is LoginFormEvent.TelefonoChanged -> _uiState.update { it.copy(telefono = event.telefono, error = null) }

            LoginFormEvent.Submit -> loginUsuario()
            LoginFormEvent.Register -> registerUsuario()
        }
    }

    private fun loginUsuario() {
        if (_uiState.value.correo.isBlank() || _uiState.value.clave.isBlank()) {
            _uiState.update { it.copy(error = "Correo y clave son obligatorios.") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(cargando = true, error = null) }

                val correo = _uiState.value.correo
                val clave = _uiState.value.clave
                val result = repository.login(correo, clave)

                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                loginExitoso = true,
                                usuarioLogueado = result.data.toUsuario(),
                                cargando = false
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.exception.message ?: "Error de conexión",
                                cargando = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error fatal de red: ${e.message}",
                        cargando = false
                    )
                }
            }
        }
    }

    private fun registerUsuario() {
        val uiStateValue = _uiState.value

        if (uiStateValue.nombre.isBlank() || uiStateValue.correo.isBlank() ||
            uiStateValue.clave.isBlank() || uiStateValue.telefono.isBlank()
        ) {
            _uiState.update { it.copy(error = "Todos los campos de registro son obligatorios.") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(cargando = true, error = null) }

                val request = RegisterRequest(
                    nombre = uiStateValue.nombre,
                    correo = uiStateValue.correo,
                    clave = uiStateValue.clave,
                    telefono = uiStateValue.telefono
                )

                val result = repository.register(request)

                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                registroExitoso = true,
                                cargando = false,
                                error = null,
                                clave = ""
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.exception.message
                                    ?: "Error al intentar registrar el usuario.",
                                cargando = false
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error fatal de red: ${e.message}",
                        cargando = false
                    )
                }
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.update {
                it.copy(
                    usuarioLogueado = null,
                    loginExitoso = false,
                    correo = "",
                    clave = ""
                )
            }
        }
    }
}