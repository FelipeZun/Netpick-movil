package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.netpick_movil.data.remote.dao.UsuarioDao

class UsuarioViewModelFactory(
    private val dao: UsuarioDao,
    private val correoUsuario: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            val viewModel = UsuarioViewModel(dao)
            correoUsuario?.let { viewModel.cargarUsuarioPorCorreo(it) }
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
