package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.model.Categoria
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest {

    private lateinit var viewModel: CategoryViewModel
    private val repository: CategoryRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CategoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCategorias exitoso actualiza lista`() = runTest {
        val listaFalsa = listOf(Categoria(1, "Acción", "Peliculas de acción"))
        coEvery { repository.getCategorias() } returns Response.success(listaFalsa)

        viewModel.loadCategorias()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.categorias.size)
        assertEquals("Acción", state.categorias[0].nombre)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadCategorias errorAPI actualiza error`() = runTest {
        val errorBody = "Not Found".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { repository.getCategorias() } returns Response.error(404, errorBody)

        viewModel.loadCategorias()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.categorias.isEmpty())
        assertEquals("Error 404", state.error)
    }

    @Test
    fun `loadProductos carga correctamente`() = runTest {
        val idCategoria = 1
        val listaProductos = listOf(mockk<Producto>(relaxed = true))
        coEvery { repository.obtenerProductosPorCategoria(idCategoria) } returns listaProductos

        viewModel.loadProductos(idCategoria)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.productos.size)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loadProductos maneja excepciones`() = runTest {
        val exceptionMsg = "Sin internet"
        coEvery { repository.obtenerProductosPorCategoria(any()) } throws RuntimeException(exceptionMsg)

        viewModel.loadProductos(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(exceptionMsg, state.error)
        assertFalse(state.isLoading)
    }
}
