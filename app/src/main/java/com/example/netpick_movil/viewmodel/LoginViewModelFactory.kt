package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.netpick_movil.data.remote.dao.UsuarioDao

class LoginViewModelFactory(private val dao: UsuarioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}