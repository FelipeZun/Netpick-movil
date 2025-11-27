package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.model.LoginRequest
import com.example.netpick_movil.model.LoginResponse
import com.example.netpick_movil.model.Usuario
import retrofit2.Response


class UsuarioRepository {

    suspend fun obtenerUsuario(id: Int): Response<Usuario> {
        return RetrofitClient.apiService.getUsuario(id)
    }

    suspend fun login(loginReq: LoginRequest): Response<LoginResponse> {
        return RetrofitClient.apiService.login(loginReq)
    }
}
