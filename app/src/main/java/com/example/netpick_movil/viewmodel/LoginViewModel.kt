package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val dao: UsuarioDao) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.CorreoChanged -> {
                _uiState.update { it.copy(correo = event.correo) }
            }
            is LoginFormEvent.ClaveChanged -> {
                _uiState.update { it.copy(clave = event.clave) }
            }
            is LoginFormEvent.Submit -> {
                loginUsuario()
            }
        }
    }

    private fun loginUsuario() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(cargando = true, error = null) }

                val usuario = dao.login(_uiState.value.correo, _uiState.value.clave)

                if (usuario != null) {
                    _uiState.update {
                        it.copy(
                            loginExitoso = true,
                            usuarioLogueado = usuario,
                            cargando = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Credenciales incorrectas",
                            cargando = false
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error: ${e.message}",
                        cargando = false
                    )
                }
            }
        }
    }
}

data class LoginUIState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val loginExitoso: Boolean = false,
    val error: String? = null,
    val usuarioLogueado: Usuario? = null
)

sealed interface LoginFormEvent {
    data class CorreoChanged(val correo: String) : LoginFormEvent
    data class ClaveChanged(val clave: String) : LoginFormEvent
    object Submit : LoginFormEvent
}