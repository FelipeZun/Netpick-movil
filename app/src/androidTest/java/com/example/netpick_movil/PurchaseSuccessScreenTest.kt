package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class PurchaseSuccessScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun muestra_elementos_de_exito_y_precio_formateado() {
        val precioPrueba = 1599.50

        composeTestRule.setContent {
            PurchaseSuccessScreen(
                navController = navController,
                totalPrice = precioPrueba
            )
        }

        composeTestRule.onNodeWithText("Compra Exitosa").assertIsDisplayed()
        composeTestRule.onNodeWithText("¡Compra Realizada!").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Compra exitosa").assertIsDisplayed()
        val precioEsperado = "$1599.50"
        composeTestRule.onNodeWithText(precioEsperado).assertIsDisplayed()
    }

    @Test
    fun boton_seguir_comprando_navega_a_home() {
        composeTestRule.setContent {
            PurchaseSuccessScreen(
                navController = navController,
                totalPrice = 100.0
            )
        }

        composeTestRule.onNodeWithText("Seguir Comprando").performClick()
        verify {
            navController.popBackStack()
            navController.navigate("home")
        }
    }

    @Test
    fun boton_volver_al_carrito_hace_pop_back_stack() {
        composeTestRule.setContent {
            PurchaseSuccessScreen(
                navController = navController,
                totalPrice = 100.0
            )
        }

        composeTestRule.onNodeWithText("Volver al Carrito").performClick()
        verify { navController.popBackStack() }
    }
}