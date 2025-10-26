package com.example.netpick_movil.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductDetailViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val favoritesViewModel: FavoritesViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            return ProductDetailViewModel(savedStateHandle, favoritesViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}