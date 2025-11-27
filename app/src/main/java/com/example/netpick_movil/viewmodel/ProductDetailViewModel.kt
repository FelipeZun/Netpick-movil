package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.repository.ProductoRepository
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailState(
    val product: Producto? = null,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProductDetailViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState: StateFlow<ProductDetailState> = _uiState
    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            if (_uiState.value.product?.idProducto == productId) {
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val product = repository.obtenerProducto(productId)

                _uiState.update {
                    it.copy(
                        product = product,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar el producto $productId"
                    )
                }
            }
        }
    }

    fun onQuantityChanged(newValue: Int) {
        _uiState.update { it.copy(quantity = newValue) }
    }
}
