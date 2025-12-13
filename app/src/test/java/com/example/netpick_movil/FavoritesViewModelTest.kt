package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.model.Producto
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {
    private lateinit var viewModel: FavoritesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoritesViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleFavorite agrega producto si no esta en favoritos`() = runTest {
        val producto = mockk<Producto>(relaxed = true)
        every { producto.idProducto } returns 99
        viewModel.toggleFavorite(producto)
        val state = viewModel.uiState.value
        assertEquals(1, state.favoriteProducts.size)
        assertTrue(viewModel.isFavorite(99))
    }

    @Test
    fun `toggleFavorite elimina producto si ya estaba en favoritos`() = runTest {
        val producto = mockk<Producto>(relaxed = true)
        every { producto.idProducto } returns 99
        viewModel.toggleFavorite(producto)
        assertTrue(viewModel.isFavorite(99))
        viewModel.toggleFavorite(producto)
        val state = viewModel.uiState.value
        assertTrue(state.favoriteProducts.isEmpty())
        assertFalse(viewModel.isFavorite(99))
    }
}