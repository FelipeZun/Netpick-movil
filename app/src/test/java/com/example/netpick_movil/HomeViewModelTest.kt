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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val apiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga productos exitosamente`() = runTest {
        val producto1 = mockk<Producto>(relaxed = true)
        coEvery { producto1.nombre } returns "Notebook Gamer"

        val producto2 = mockk<Producto>(relaxed = true)
        coEvery { producto2.nombre } returns "Mouse Inalámbrico"

        val listaProductos = listOf(producto1, producto2)
        coEvery { apiService.listarProductos() } returns Response.success(listaProductos)

        viewModel = HomeViewModel(apiService)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.productos.size)
        assertEquals(2, state.productosFiltrados.size)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `init maneja error de API correctamente`() = runTest {
        val errorBody = mockk<ResponseBody>(relaxed = true)
        coEvery { apiService.listarProductos() } returns Response.error(500, errorBody)

        viewModel = HomeViewModel(apiService)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.productos.isEmpty())
        assertTrue(state.error?.contains("500") == true)
        assertFalse(state.isLoading)
    }

    @Test
    fun `init maneja excepcion de red`() = runTest {
        val errorMsg = "No internet connection"
        coEvery { apiService.listarProductos() } throws RuntimeException(errorMsg)

        viewModel = HomeViewModel(apiService)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(errorMsg, state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `onSearchQueryChanged filtra correctamente la lista`() = runTest {
        val p1 = mockk<Producto>(relaxed = true)
        coEvery { p1.nombre } returns "iPhone 15"
        coEvery { p1.descripcion } returns "Apple phone"

        val p2 = mockk<Producto>(relaxed = true)
        coEvery { p2.nombre } returns "Samsung S24"
        coEvery { p2.descripcion } returns "Android phone"

        coEvery { apiService.listarProductos() } returns Response.success(listOf(p1, p2))

        viewModel = HomeViewModel(apiService)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("Samsung")

        val state = viewModel.uiState.value
        assertEquals("Samsung", state.searchQuery)
        assertEquals(1, state.productosFiltrados.size)
        assertEquals("Samsung S24", state.productosFiltrados[0].nombre)
    }

    @Test
    fun `onSearchQueryChanged vacio restaura la lista completa`() = runTest {
        val p1 = mockk<Producto>(relaxed = true); coEvery { p1.nombre } returns "A"
        val p2 = mockk<Producto>(relaxed = true); coEvery { p2.nombre } returns "B"

        coEvery { apiService.listarProductos() } returns Response.success(listOf(p1, p2))

        viewModel = HomeViewModel(apiService)
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("A")
        assertEquals(1, viewModel.uiState.value.productosFiltrados.size)

        viewModel.onSearchQueryChanged("")
        assertEquals(2, viewModel.uiState.value.productosFiltrados.size)
    }
}
