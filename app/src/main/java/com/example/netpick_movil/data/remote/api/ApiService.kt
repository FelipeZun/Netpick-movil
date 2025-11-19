package com.example.netpick_movil.data.remote.api

import com.example.netpick_movil.model.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>
}