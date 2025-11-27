package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.data.repository.SessionManager


class RegisterViewModelFactory(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authApi, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
