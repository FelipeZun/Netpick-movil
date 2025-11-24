package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.repository.ProductoRepository
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
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    private var productosOriginales: List<Producto> = emptyList()

    init {
        fetchProductos()
    }

    private fun fetchProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val productosApi = repository.obtenerProductos()

            productosOriginales = productosApi

            _uiState.update {
                it.copy(
                    productos = productosOriginales,
                    productosFiltrados = productosOriginales,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val productosFiltrados = if (query.isBlank()) {
                productosOriginales
            } else {
                filtrarProductos(query, productosOriginales)
            }

            currentState.copy(
                searchQuery = query,
                productosFiltrados = productosFiltrados
            )
        }
    }

    private fun filtrarProductos(query: String, productos: List<Producto>): List<Producto> {
        val queryLowercase = query.lowercase().trim()

        return productos.filter { producto ->
            producto.nombre.lowercase().contains(queryLowercase) ||
                    producto.description.lowercase().contains(queryLowercase)
        }
    }
}