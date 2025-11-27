package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.model.Usuario
import retrofit2.Response

class ProductoRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerProductos(): List<Producto> {
        return try {
            val response = api.listarProductos()
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

    suspend fun obtenerProducto(id: Int): Producto? {
        return try {
            val response = RetrofitClient.apiService.getProducto(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string() ?: "Producto no encontrado"}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

}