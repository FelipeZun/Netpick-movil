package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.HomeUIState
import com.example.netpick_movil.viewmodel.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<HomeViewModel>(relaxed = true)
    private val uiStateFlow = MutableStateFlow(HomeUIState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun muestra_loading_cuando_esta_cargando() {
        uiStateFlow.value = HomeUIState(isLoading = true)

        setupScreen()

        composeTestRule.onNodeWithText("Cargando productos...").assertIsDisplayed()
    }

    @Test
    fun muestra_error_cuando_hay_fallo() {
        uiStateFlow.value = HomeUIState(error = "404 Not Found")

        setupScreen()

        composeTestRule.onNodeWithText("Error al cargar: 404 Not Found").assertIsDisplayed()
    }

    @Test
    fun muestra_lista_de_productos_cuando_hay_datos() {
        val catDummy = Categoria(1, "Test", "img")
        val prod1 = Producto(1, "Laptop", "Desc", 1000, 5, "img", catDummy)
        val prod2 = Producto(2, "Mouse", "Desc", 50, 10, "img", catDummy)

        uiStateFlow.value = HomeUIState(productosFiltrados = listOf(prod1, prod2))

        setupScreen()
        composeTestRule.waitForIdle()

        // VERIFICACIÓN CORREGIDA

        // 1. Laptop
        composeTestRule.onNodeWithText("Laptop", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("$ 1000", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()

        // 2. Mouse
        composeTestRule.onNodeWithText("Mouse", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("$ 50", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun escribir_en_buscador_llama_al_viewmodel() {
        setupScreen()

        composeTestRule.onNodeWithText("Buscar productos").performTextInput("Teclado")

        verify { viewModel.onSearchQueryChanged("Teclado") }
    }

    @Test
    fun click_en_producto_navega_al_detalle() {
        val catDummy = Categoria(1, "Test", "img")
        val prod1 = Producto(99, "Monitor", "Desc", 200, 5, "img", catDummy)

        uiStateFlow.value = HomeUIState(productosFiltrados = listOf(prod1))

        setupScreen()

        composeTestRule.onNodeWithText("Monitor").performClick()

        verify { navController.navigate(any<String>()) }
    }
}