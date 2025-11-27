package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.model.LoginRequest
import com.example.netpick_movil.model.RegisterRequest
import com.example.netpick_movil.model.UserSession
import com.example.netpick_movil.model.toUsuario
import retrofit2.Response

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
class SessionManager {
    private var currentUser: UserSession? = null

    fun saveSession(user: UserSession) {
        currentUser = user
    }

    fun getSession(): UserSession? = currentUser

    fun clearSession() {
        currentUser = null
    }
}


class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {
    suspend fun login(correo: String, clave: String): Result<UserSession> {
        return try {
            val response = authApi.login(LoginRequest(correo, clave))

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val userSession = UserSession(
                    userId = loginResponse.userId,
                    nombre = loginResponse.nombre,
                    correo = correo,
                    rol = loginResponse.rol
                )

                sessionManager.saveSession(userSession)
                Result.Success(userSession)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.Error(Exception("Error en credenciales o servidor: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<String> {
        return try {
            val response = authApi.register(request)

            if (response.isSuccessful) {
                Result.Success("Registro exitoso")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error al registrar"
                Result.Error(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getLoggedInUser(): UserSession? {
        return sessionManager.getSession()
    }

    fun logout() {
        sessionManager.clearSession()
    }
}