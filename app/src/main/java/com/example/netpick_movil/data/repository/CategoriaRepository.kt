package com.example.netpick_movil.data.repository

import com.example.netpick_movil.data.remote.api.ApiService

class CategoryRepository(private val api: ApiService) {

    suspend fun getCategorias() = api.listarCategorias()

    suspend fun getProductosByCategoria(idCategoria: Int) =
        api.listarProductosPorCategoria(idCategoria)
}
