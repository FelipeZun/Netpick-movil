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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addToCart agrega un producto nuevo correctamente`() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        every { productoMock.idProducto } returns 1
        every { productoMock.precio } returns 1000

        viewModel.addToCart(productoMock, 2)

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(2, state.cartItems[0].quantity)
        assertEquals(2000.0, state.totalPrice, 0.0)
    }

    @Test
    fun `addToCart suma cantidad si el producto ya existe`() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        every { productoMock.idProducto } returns 1
        every { productoMock.precio } returns 1000

        viewModel.addToCart(productoMock, 1)
        viewModel.addToCart(productoMock, 3)

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(4, state.cartItems[0].quantity)
        assertEquals(4000.0, state.totalPrice, 0.0)
    }

    @Test
    fun `updateQuantity modifica la cantidad y recalcula total`() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        every { productoMock.idProducto } returns 10
        every { productoMock.precio } returns 500

        viewModel.addToCart(productoMock, 1)
        viewModel.updateQuantity(10, 5)

        val state = viewModel.uiState.value
        assertEquals(5, state.cartItems[0].quantity)
        assertEquals(2500.0, state.totalPrice, 0.0)
    }

    @Test
    fun `updateQuantity elimina el producto si la cantidad es 0`() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        every { productoMock.idProducto } returns 10
        every { productoMock.precio } returns 500

        viewModel.addToCart(productoMock, 2)
        viewModel.updateQuantity(10, 0)

        val state = viewModel.uiState.value
        assertTrue(state.cartItems.isEmpty())
        assertEquals(0.0, state.totalPrice, 0.0)
    }

    @Test
    fun `removeFromCart elimina el producto del carrito`() = runTest {
        val p1 = mockk<Producto>(relaxed = true)
        every { p1.idProducto } returns 1
        every { p1.precio } returns 100

        val p2 = mockk<Producto>(relaxed = true)
        every { p2.idProducto } returns 2
        every { p2.precio } returns 200

        viewModel.addToCart(p1, 1)
        viewModel.addToCart(p2, 1)

        viewModel.removeFromCart(1)

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(2, state.cartItems[0].product.idProducto)
        assertEquals(200.0, state.totalPrice, 0.0)
    }

    @Test
    fun `clearCart vacia todo el carrito`() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        every { productoMock.idProducto } returns 1
        every { productoMock.precio } returns 1000

        viewModel.addToCart(productoMock, 5)
        viewModel.clearCart()

        val state = viewModel.uiState.value
        assertTrue(state.cartItems.isEmpty())
        assertEquals(0.0, state.totalPrice, 0.0)
    }
}