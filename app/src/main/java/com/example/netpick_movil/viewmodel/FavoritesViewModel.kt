package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<List<Producto>>(emptyList())
    val favorites: StateFlow<List<Producto>> = _favorites.asStateFlow()

    fun addToFavorites(product: Producto) {
        viewModelScope.launch {
            val currentFavorites = _favorites.value.toMutableList()
            if (!currentFavorites.any { it.id == product.id }) {
                currentFavorites.add(product)
                _favorites.value = currentFavorites
            }
        }
    }

    fun removeFromFavorites(productId: String) {
        viewModelScope.launch {
            val currentFavorites = _favorites.value.toMutableList()
            currentFavorites.removeAll { it.id == productId }
            _favorites.value = currentFavorites
        }
    }

    fun isFavorite(productId: String): Boolean {
        return _favorites.value.any { it.id == productId }
    }
}