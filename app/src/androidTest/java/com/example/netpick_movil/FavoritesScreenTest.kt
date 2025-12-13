package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.FavoritesUiState
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks
    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<FavoritesViewModel>(relaxed = true)

    private val uiStateFlow = MutableStateFlow(FavoritesUiState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            FavoritesScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun muestra_mensaje_vacio_cuando_no_hay_favoritos() { uiStateFlow.value = FavoritesUiState(favoriteProducts = emptyList())

        setupScreen()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("No tienes favoritos aún").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explorar productos").assertIsDisplayed()
    }

    @Test
    fun muestra_lista_cuando_hay_favoritos() {
        val catDummy = Categoria(1, "Test", "ola")
        val prod1 = Producto(10, "Zapatillas Nike", "Desc", 20000 , 5, "url", catDummy)
        val prod2 = Producto(11, "Gorra Adidas", "Desc", 35000, 5, "url", catDummy)

        uiStateFlow.value = FavoritesUiState(favoriteProducts = listOf(prod1, prod2))

        setupScreen()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("2 productos guardados").assertIsDisplayed()

        composeTestRule.onNodeWithText("Zapatillas Nike").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gorra Adidas").assertIsDisplayed()

        composeTestRule.onNodeWithText("$20000").assertIsDisplayed()
        composeTestRule.onNodeWithText("$35000").assertIsDisplayed()

    }

    @Test
    fun navegar_al_detalle_al_hacer_click_en_producto() {
        val catDummy = Categoria(1, "Test", "img")
        val prod1 = Producto(99, "Producto Click", "img", 2000, 1, "url", catDummy)

        uiStateFlow.value = FavoritesUiState(favoriteProducts = listOf(prod1))

        setupScreen()
        composeTestRule.onNodeWithText("Producto Click").performClick()
        verify { navController.navigate("product_detail/99") }
    }

    @Test
    fun boton_quitar_llama_al_viewmodel() {
        val catDummy = Categoria(1, "Test", "img")
        val prod1 = Producto(55, "Producto Borrar", "alo", 3000, 1, "url", catDummy)

        uiStateFlow.value = FavoritesUiState(favoriteProducts = listOf(prod1))

        setupScreen()

        composeTestRule.onNodeWithText("Quitar").performClick()

        verify { viewModel.toggleFavorite(prod1) }
    }

    @Test
    fun boton_explorar_navega_atras_en_estado_vacio() {
        uiStateFlow.value = FavoritesUiState(favoriteProducts = emptyList())
        setupScreen()

        composeTestRule.onNodeWithText("Explorar productos").performClick()

        verify { navController.popBackStack() }
    }
}