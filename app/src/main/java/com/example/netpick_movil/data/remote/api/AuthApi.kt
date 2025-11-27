package com.example.netpick_movil.data.remote.api

import com.example.netpick_movil.model.Direccion
import com.example.netpick_movil.model.LoginRequest
import com.example.netpick_movil.model.LoginResponse
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.model.ProductosResponse
import com.example.netpick_movil.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.netpick_movil.model.RegisterRequest;

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
