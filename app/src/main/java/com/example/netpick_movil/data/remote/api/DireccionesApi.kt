package com.example.netpick_movil.data.remote.api

import com.example.netpick_movil.model.Direccion
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DireccionesApi {
    @GET("direcciones/{id}")
    suspend fun getDireccion(@Path("id") id: Int): Response<Direccion>
}