package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.netpick_movil.data.repository.AuthRepository
import com.example.netpick_movil.data.repository.SessionManager
import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.data.remote.api.RetrofitClient

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val authApi = RetrofitClient.retrofitInstance.create(AuthApi::class.java)
                val sessionManager = SessionManager()
                val authRepository = AuthRepository(
                    authApi = authApi,
                    sessionManager = sessionManager
                )
                AuthViewModel(authRepository)
            }
        }
    }
}