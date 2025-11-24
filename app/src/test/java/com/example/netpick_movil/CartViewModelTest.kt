package com.example.netpick_movil

import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CartViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = CartViewModel()

    private val fakeProduct = Producto(
        id = "1",
        nombre = "Producto Test",
        precio = 100.0,
        imageUrls = emptyList(),
        description = "Descripción"
    )

    @Test
    fun `addToCart agrega un item nuevo correctamente`() {
        viewModel.addToCart(fakeProduct, 1)

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(100.0, state.totalPrice, 0.0)
    }

    @Test
    fun `addToCart suma cantidad si el producto ya existe`() {
        viewModel.addToCart(fakeProduct, 1)
        viewModel.addToCart(fakeProduct, 2)

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(3, state.cartItems[0].quantity)
        assertEquals(300.0, state.totalPrice, 0.0)
    }

    @Test
    fun `updateQuantity modifica la cantidad y recalcula precio`() {
        viewModel.addToCart(fakeProduct, 1)
        viewModel.updateQuantity("1", 5)

        val state = viewModel.uiState.value
        assertEquals(5, state.cartItems[0].quantity)
        assertEquals(500.0, state.totalPrice, 0.0)
    }

    @Test
    fun `removeFromCart elimina el producto`() {
        viewModel.addToCart(fakeProduct, 1)
        viewModel.removeFromCart("1")

        val state = viewModel.uiState.value
        assertEquals(0, state.cartItems.size)
        assertEquals(0.0, state.totalPrice, 0.0)
    }

    @Test
    fun `clearCart vacia todo el carrito`() {
        viewModel.addToCart(fakeProduct, 5)
        viewModel.clearCart()

        val state = viewModel.uiState.value
        assertEquals(0, state.cartItems.size)
        assertEquals(0.0, state.totalPrice, 0.0)
    }
}