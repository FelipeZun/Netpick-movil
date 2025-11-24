package com.example.netpick_movil.domain.usuario

import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.Usuario

class RegistrarUsuarioTest(private val usuarioDao: UsuarioDao) {
    suspend operator fun invoke(usuario: Usuario) {
        usuarioDao.registrar(usuario)
    }
}
