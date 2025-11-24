package com.example.netpick_movil

import com.example.netpick_movil.data.repository.ProductoRepository
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepo = mockk<ProductoRepository>()

    private val listaProductos = listOf(
        Producto("1", "Iphone 15", 1000.0, listOf(), "Apple phone"),
        Producto("2", "Samsung S24", 900.0, listOf(), "Android phone"),
        Producto("3", "Mouse Gamer", 50.0, listOf(), "Accesorios")
    )

    @Test
    fun initLoadsInitialProducts() {
        coEvery { mockRepo.obtenerProductos() } returns listaProductos

        val viewModel = HomeViewModel(mockRepo)

        assertEquals(3, viewModel.uiState.value.productos.size)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun searchFiltersCorrectly() {
        coEvery { mockRepo.obtenerProductos() } returns listaProductos
        val viewModel = HomeViewModel(mockRepo)

        viewModel.onSearchQueryChanged("Samsung")

        val filtrados = viewModel.uiState.value.productosFiltrados
        assertEquals(1, filtrados.size)
        assertEquals("Samsung S24", filtrados.first().nombre)
    }

    @Test
    fun emptySearchReturnsAllProducts() {
        coEvery { mockRepo.obtenerProductos() } returns listaProductos
        val viewModel = HomeViewModel(mockRepo)

        viewModel.onSearchQueryChanged("")

        assertEquals(3, viewModel.uiState.value.productosFiltrados.size)
    }
}