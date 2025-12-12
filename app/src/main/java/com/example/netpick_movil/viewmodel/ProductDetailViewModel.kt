package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.data.remote.api.ApiService
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductDetailUIState(
    val product: Producto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProductDetailViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState: StateFlow<ProductDetailUIState> = _uiState

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUIState(isLoading = true)

            try {
                val response = apiService.getProducto(productId)

                if (response.isSuccessful) {
                    _uiState.value = ProductDetailUIState(
                        product = response.body(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = ProductDetailUIState(
                        isLoading = false,
                        errorMessage = "Error ${response.code()}"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = ProductDetailUIState(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error desconocido"
                )
            }
        }
    }
}
