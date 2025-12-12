package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryUiState(
    val categorias: List<Categoria> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun loadCategorias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = repository.getCategorias()
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            categorias = response.body() ?: emptyList(),
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Error ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun loadProductos(idCategoria: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val productosFiltrados = repository.obtenerProductosPorCategoria(idCategoria)

                _uiState.update {
                    it.copy(
                        productos = productosFiltrados,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }
}