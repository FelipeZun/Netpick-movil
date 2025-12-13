package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.viewmodel.CategoryUiState
import com.example.netpick_movil.viewmodel.CategoryViewModel
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class CategoriesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<CategoryViewModel>(relaxed = true)

    private val uiStateFlow = MutableStateFlow(CategoryUiState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()
        every { viewModel.loadCategorias() } just runs

        composeTestRule.setContent {
            CategoriesScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun elementos_iniciales_se_muestran() {
        setupScreen()
        composeTestRule.onNodeWithText("Categorías").assertIsDisplayed()
    }

    @Test
    fun muestra_mensaje_de_error() {
        uiStateFlow.value = CategoryUiState(error = "Fallo de conexión")

        setupScreen()

        composeTestRule.onNodeWithText("Error: Fallo de conexión").assertIsDisplayed()
    }

    @Test
    fun muestra_lista_categorias_correctamente() {
        val cat1 = Categoria(idCategoria = 1, nombre = "Tecnología", descripcion = "prueba")
        val cat2 = Categoria(idCategoria = 2, nombre = "Hogar", descripcion = "prueba")

        uiStateFlow.value = CategoryUiState(categorias = listOf(cat1, cat2))

        setupScreen()

        composeTestRule.onNodeWithText("Tecnología").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hogar").assertIsDisplayed()
    }

    @Test
    fun click_categoria_navega_con_argumentos() {
        val cat = Categoria(idCategoria = 99, nombre = "Deportes", descripcion = "prueba")
        uiStateFlow.value = CategoryUiState(categorias = listOf(cat))

        setupScreen()

        composeTestRule.onNodeWithText("Deportes").performClick()

        val encodedName = URLEncoder.encode("Deportes", StandardCharsets.UTF_8.toString())
        val expectedRoute = "category/99?name=$encodedName"

        verify { navController.navigate(expectedRoute) }
    }
}