package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.remote.api.ApiService
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUIState(
    val searchQuery: String = "",
    val productos: List<Producto> = emptyList(),
    val productosFiltrados: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        println("DEBUG_VM: HomeViewModel inicializado")
        obtenerProductos()
    }

    fun obtenerProductos() {
        println("DEBUG_API: Iniciando petición de productos...")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val response = apiService.listarProductos()

                println("DEBUG_API: Código de respuesta = ${response.code()}")

                if (response.isSuccessful) {
                    val productos = response.body() ?: emptyList()

                    println("DEBUG_API: Productos recibidos (${productos.size}) =")
                    productos.forEach { p ->
                        println("  -> ID=${p.idProducto}, Nombre=${p.nombre}")
                    }

                    _uiState.update {
                        it.copy(
                            productos = productos,
                            productosFiltrados = productos,
                            isLoading = false
                        )
                    }
                } else {
                    println("DEBUG_API_ERROR: Error en la API = ${response.code()}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error ${response.code()}"
                        )
                    }
                }

            } catch (e: Exception) {
                println("DEBUG_EXCEPTION: Excepción al obtener productos = ${e.message}")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        println("DEBUG_SEARCH: Query nueva = $query")

        _uiState.update { currentState ->
            val productosFiltrados = if (query.isBlank()) {
                println("DEBUG_SEARCH: Query vacía → mostrando todos")
                currentState.productos
            } else {
                val filtrados = currentState.productos.filter { producto ->
                    (producto.nombre ?: "").contains(query, ignoreCase = true) ||
                            (producto.descripcion ?: "").contains(query, ignoreCase = true)
                }

                println("DEBUG_SEARCH: ${filtrados.size} resultados encontrados")

                filtrados
            }

            currentState.copy(
                searchQuery = query,
                productosFiltrados = productosFiltrados
            )
        }
    }
}
