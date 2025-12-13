package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.data.remote.api.ApiService
import com.example.netpick_movil.model.Producto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private lateinit var viewModel: ProductDetailViewModel
    private val apiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductDetailViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProduct_exitoso_actualizaEstado() = runTest {
        val productoMock = mockk<Producto>(relaxed = true)
        coEvery { apiService.getProducto(1) } returns Response.success(productoMock)

        viewModel.loadProduct(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.product)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun loadProduct_errorApi_actualizaMensajeError() = runTest {
        val errorBody = mockk<ResponseBody>(relaxed = true)
        coEvery { apiService.getProducto(1) } returns Response.error(404, errorBody)

        viewModel.loadProduct(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.product)
        assertFalse(state.isLoading)
        assertEquals("Error 404", state.errorMessage)
    }

    @Test
    fun loadProduct_excepcion_actualizaMensajeError() = runTest {
        val mensajeError = "Sin conexión"
        coEvery { apiService.getProducto(any()) } throws RuntimeException(mensajeError)

        viewModel.loadProduct(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.product)
        assertFalse(state.isLoading)
        assertEquals(mensajeError, state.errorMessage)
    }
}