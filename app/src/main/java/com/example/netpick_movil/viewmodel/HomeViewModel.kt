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
        obtenerProductos()
    }

    fun obtenerProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = apiService.listarProductos()
                if (response.isSuccessful) {
                    val productos = response.body() ?: emptyList()
                    _uiState.update {
                        it.copy(
                            productos = productos,
                            productosFiltrados = productos,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val productosFiltrados = if (query.isBlank()) {
                currentState.productos
            } else {
                currentState.productos.filter { producto ->
                    (producto.nombre ?: "").contains(query, ignoreCase = true) ||
                            (producto.descripcion ?: "").contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                productosFiltrados = productosFiltrados
            )
        }
    }
}
