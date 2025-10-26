package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FavoritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun toggleFavorite(product: Producto) {
        _uiState.update { currentState ->
            val favoriteProducts = currentState.favoriteProducts.toMutableList()
            if (favoriteProducts.any { it.id == product.id }) {
                favoriteProducts.removeAll { it.id == product.id }
            } else {
                favoriteProducts.add(product)
            }
            currentState.copy(favoriteProducts = favoriteProducts)
        }
    }

    fun isFavorite(productId: String): Boolean {
        return _uiState.value.favoriteProducts.any { it.id == productId }
    }
}

data class FavoritesUiState(
    val favoriteProducts: List<com.example.netpick_movil.model.Producto> = emptyList()
)