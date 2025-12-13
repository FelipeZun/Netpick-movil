package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesUiState
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailUIState
import com.example.netpick_movil.viewmodel.ProductDetailViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class ProductDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<ProductDetailViewModel>(relaxed = true)

    private val cartViewModel = mockk<CartViewModel>(relaxed = true)
    private val favoritesViewModel = mockk<FavoritesViewModel>(relaxed = true)
    private val uiStateFlow = MutableStateFlow(ProductDetailUIState())

    private val favoritesUiStateFlow = MutableStateFlow(FavoritesUiState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()
        every { favoritesViewModel.uiState } returns favoritesUiStateFlow.asStateFlow()

        composeTestRule.setContent {
            ProductDetailScreen(
                navController = navController,
                viewModel = viewModel,
                cartViewModel = cartViewModel,
                favoritesViewModel = favoritesViewModel,
                productId = 1
            )
        }
    }

    @Test
    fun muestra_informacion_producto_correctamente() {
        val categoriaMock = mockk<Categoria>(relaxed = true)

        val productoFicticio = Producto(
            idProducto = 1,
            nombre = "Camiseta Cool",
            precio = 15000,
            descripcion = "Una camiseta de prueba",
            stock = 10,
            linkImagen = "http://fake.url/img.png",
            categoria = categoriaMock
        )
        uiStateFlow.value = ProductDetailUIState(product = productoFicticio)

        setupScreen()

        composeTestRule.waitForIdle()
        composeTestRule.onRoot().printToLog("ARBOL_UI")

        // Verificaciones
        composeTestRule.onNodeWithText("Camiseta Cool").assertIsDisplayed()

        // CAMBIO 2: Agrega performScrollTo() para la descripción
        // (La descripción suele estar abajo, así que bajamos hasta encontrarla)
        composeTestRule.onNodeWithText("Una camiseta de prueba")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("15000", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun muestra_error_cuando_falla_carga() {
        setupScreen()
        uiStateFlow.value = ProductDetailUIState(errorMessage = "No se pudo cargar el producto")

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("No se pudo cargar el producto").assertIsDisplayed()
    }

    @Test
    fun muestra_loading_cuando_esta_cargando() {
        setupScreen()
        uiStateFlow.value = ProductDetailUIState(isLoading = true)
        composeTestRule.waitForIdle()
    }
}