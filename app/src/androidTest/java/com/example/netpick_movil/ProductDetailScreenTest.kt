package com.example.netpick_movil

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo // IMPORTANTE
import androidx.navigation.NavController
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.ui.screen.ProductDetailScreen
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailState
import com.example.netpick_movil.viewmodel.ProductDetailViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ProductDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productDetailScreen_displaysProductInfo() {
        val navController = mockk<NavController>(relaxed = true)
        val viewModel = mockk<ProductDetailViewModel>()
        val cartViewModel = mockk<CartViewModel>(relaxed = true)
        val favoritesViewModel = mockk<FavoritesViewModel>()

        val fakeProduct = Producto(
            id = "99",
            nombre = "Celular de Prueba",
            precio = 50000.0,
            imageUrls = listOf("http://fake.url"),
            description = "Descripción de prueba para el test"
        )

        val stateFlow = MutableStateFlow(ProductDetailState(product = fakeProduct))
        every { viewModel.uiState } returns stateFlow
        every { favoritesViewModel.isFavorite("99") } returns false

        composeTestRule.setContent {
            ProductDetailScreen(
                navController = navController,
                productId = "99",
                viewModel = viewModel,
                cartViewModel = cartViewModel,
                favoritesViewModel = favoritesViewModel
            )
        }

        composeTestRule.onNodeWithText("Celular de Prueba").assertIsDisplayed()
        composeTestRule.onNodeWithText("50000", substring = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripción de prueba para el test").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Añadir", substring = true, ignoreCase = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Comprar", substring = true, ignoreCase = true).performScrollTo().assertIsDisplayed()

    }
}