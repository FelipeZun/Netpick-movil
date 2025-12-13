package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CartItem
import com.example.netpick_movil.viewmodel.CartUiState
import com.example.netpick_movil.viewmodel.CartViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<CartViewModel>(relaxed = true)
    private val uiStateFlow = MutableStateFlow(CartUiState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            CartScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun muestra_estado_vacio_correctamente() {
        uiStateFlow.value = CartUiState(cartItems = emptyList())

        setupScreen()

        composeTestRule.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seguir comprando").assertIsDisplayed()
    }

    @Test
    fun muestra_items_y_total_correctamente() {
        val categoriaMock = mockk<Categoria>(relaxed = true)
        val producto = Producto(
            idProducto = 1,
            nombre = "Producto Test",
            precio = 1000,
            descripcion = "Desc",
            stock = 5,
            linkImagen = "http://img.png",
            categoria = categoriaMock
        )
        val cartItem = CartItem(product = producto, quantity = 2)

        uiStateFlow.value = CartUiState(
            cartItems = listOf(cartItem),
            totalPrice = 2000.0
        )

        setupScreen()

        composeTestRule.onNodeWithText("Producto Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("$2000.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 productos en el carrito").assertIsDisplayed()
    }

    @Test
    fun botones_cantidad_y_eliminar_funcionan() {
        val categoriaMock = mockk<Categoria>(relaxed = true)
        val producto = Producto(
            idProducto = 10,
            nombre = "Item",
            precio = 500,
            descripcion = "D",
            stock = 10,
            linkImagen = "http://img.png",
            categoria = categoriaMock
        )
        val cartItem = CartItem(product = producto, quantity = 1)

        uiStateFlow.value = CartUiState(cartItems = listOf(cartItem))

        setupScreen()

        composeTestRule.onNode(hasContentDescription("Añadir")).performClick()
        verify { viewModel.updateQuantity(10, 2) }

        composeTestRule.onNode(hasContentDescription("Disminuir")).performClick()
        verify { viewModel.updateQuantity(10, 0) }

        composeTestRule.onNode(hasContentDescription("Eliminar")).performClick()
        verify { viewModel.removeFromCart(10) }
    }

    @Test
    fun boton_comprar_limpia_y_navega() {
        val categoriaMock = mockk<Categoria>(relaxed = true)
        val producto = Producto(
            idProducto = 1,
            nombre = "P",
            precio = 100,
            descripcion = "D",
            stock = 1,
            linkImagen = "",
            categoria = categoriaMock
        )
        val cartItem = CartItem(product = producto, quantity = 1)

        uiStateFlow.value = CartUiState(cartItems = listOf(cartItem))

        setupScreen()

        composeTestRule.onNode(hasText("Comprar ahora")).performClick()

        verify { viewModel.clearCart() }
        verify { navController.navigate(any<String>()) }
    }
}