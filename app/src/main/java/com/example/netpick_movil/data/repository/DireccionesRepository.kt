package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.remote.api.DireccionesApi
import com.example.netpick_movil.model.Direccion
import retrofit2.Response

class DireccionRepository {

    private val direccionApi: DireccionesApi = RetrofitClient.retrofitInstance
        .create(DireccionesApi::class.java)

    suspend fun obtenerDireccion(id: Int): Response<Direccion> {
        return direccionApi.getDireccion(id)
    }
}