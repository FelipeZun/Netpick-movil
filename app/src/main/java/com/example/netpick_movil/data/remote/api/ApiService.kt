package com.example.netpick_movil.data.remote.api

import com.example.netpick_movil.model.Categoria
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

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginRequest): Response<LoginResponse>

    @GET("producto")
    suspend fun listarProductos(): Response<List<Producto>>
    @GET("usuarios/{id}")
    suspend fun getUsuario(
        @Path("id") id: Int
    ): Response<Usuario>

    @GET("direcciones/{id}")
    suspend fun getDireccion(
        @Path("id") id: Int
    ): Response<Direccion>

    @GET("producto/{id}")
    suspend fun getProducto(@Path("id") id: Int): Response<Producto>

    @GET("categoria")
    suspend fun listarCategorias(): Response<List<Categoria>>

    @GET("producto/categoria/{id}")
    suspend fun listarProductosPorCategoria(@Path("id") id: Int): Response<List<Producto>>
}
