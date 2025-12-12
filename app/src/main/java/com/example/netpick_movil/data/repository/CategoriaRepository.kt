package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.ApiService
import com.example.netpick_movil.model.Producto

class CategoryRepository(private val api: ApiService) {
    suspend fun getCategorias() = api.listarCategorias()

    suspend fun obtenerProductosPorCategoria(categoryId: Int): List<Producto> {
        try {
            val response = api.obtenerTodosLosProductos()

            if (response.isSuccessful) {
                val todosLosProductos = response.body() ?: emptyList()

                return todosLosProductos.filter { producto ->
                    producto.categoria?.idCategoria == categoryId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}