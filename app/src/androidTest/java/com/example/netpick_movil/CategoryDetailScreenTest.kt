package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CategoryUiState
import com.example.netpick_movil.viewmodel.CategoryViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class CategoryDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<CategoryViewModel>(relaxed = true)

    private val uiStateFlow = MutableStateFlow(CategoryUiState())

    private fun setupScreen(categoryId: Int = 1, categoryName: String = "Tecnología") { every { viewModel.uiState } returns uiStateFlow.asStateFlow()
        coEvery { viewModel.loadProductos(any()) } returns Unit

        composeTestRule.setContent {
            CategoryDetailScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun muestra_titulo_y_lista_productos_cuando_hay_datos() {
        val catDummy = Categoria(1, "Cat_Interna", "img.png")

        val productosFicticios = listOf(
            Producto(1, "Laptop Gamer", "prueba", 10000, 5, "img.png", catDummy),
            Producto(2, "Mouse USB", "prueba", 10000, 10, "img2.png", catDummy)
        )

        uiStateFlow.value = CategoryUiState(
            productos = productosFicticios,
            isLoading = false
        )

        setupScreen(categoryName = "Tecnología")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Tecnología").assertIsDisplayed()
        composeTestRule.onNodeWithText("Laptop Gamer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mouse USB").assertIsDisplayed()
    }

    @Test
    fun muestra_mensaje_cuando_lista_esta_vacia() {
        uiStateFlow.value = CategoryUiState(
            productos = emptyList(),
            isLoading = false,
            error = null
        )

        setupScreen()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("No hay productos en esta categoría").assertIsDisplayed()
    }

    @Test
    fun muestra_error_cuando_api_falla() {
        uiStateFlow.value = CategoryUiState(
            error = "Error de conexión 404",
            isLoading = false
        )

        setupScreen()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Error de conexión 404").assertIsDisplayed()
    }

    @Test
    fun muestra_titulo_categoria_correcto() {
        setupScreen(categoryName = "Ropa de Invierno")

        composeTestRule.onNodeWithText("Ropa de Invierno").assertIsDisplayed()
    }
}