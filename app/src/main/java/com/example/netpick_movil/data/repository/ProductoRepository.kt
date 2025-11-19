package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.model.Producto

class ProductoRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerProductos(): List<Producto> {
        return try {
            val response = api.getProductos()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}