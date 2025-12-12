package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CategoriesUIState(
    val categorias: List<Categoria> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUIState())
    val uiState: StateFlow<CategoriesUIState> = _uiState

    fun loadCategorias() {
        viewModelScope.launch {
            _uiState.value = CategoriesUIState(isLoading = true)

            try {
                val response = repository.getCategorias()
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        categorias = response.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = CategoriesUIState(error = "Error ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = CategoriesUIState(error = e.localizedMessage)
            }
        }
    }

    fun loadProductos(idCategoria: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = repository.getProductosByCategoria(idCategoria)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        productos = response.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(error = "Error ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.localizedMessage)
            }
        }
    }
}
