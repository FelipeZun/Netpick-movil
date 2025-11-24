package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.MainDispatcherRule
import com.example.netpick_movil.model.Producto
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = FavoritesViewModel()

    private val fakeProduct = Producto("1", "Test", 100.0, listOf(), "Desc")

    @Test
    fun toggleFavoriteAddsAndRemovesProduct() {
        viewModel.toggleFavorite(fakeProduct)
        assertTrue(viewModel.isFavorite("1"))

        viewModel.toggleFavorite(fakeProduct)
        assertFalse(viewModel.isFavorite("1"))
    }
}