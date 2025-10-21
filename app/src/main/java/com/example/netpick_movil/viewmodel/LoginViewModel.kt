package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUIState(
    val correo: String = "",
    val clave: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

sealed interface LoginFormEvent {
    data class CorreoChanged(val correo: String) : LoginFormEvent
    data class ClaveChanged(val clave: String) : LoginFormEvent
    object LoginClicked : LoginFormEvent
}

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.CorreoChanged -> _uiState.update { it.copy(correo = event.correo) }
            is LoginFormEvent.ClaveChanged -> _uiState.update { it.copy(clave = event.clave) }
            LoginFormEvent.LoginClicked -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                    // Simulación de una llamada de red
                    delay(1000)
                    if (uiState.value.correo == "test@test.com" && uiState.value.clave == "password") {
                        _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
                    }
                }
            }
        }
    }
}