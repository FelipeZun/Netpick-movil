package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ConfirmationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun muestra_elementos_visuales_correctamente() {
        composeTestRule.setContent {
            ConfirmationScreen(navController = navController)
        }
        composeTestRule.onNodeWithContentDescription("Compra Exitosa")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("¡Compra realizada con éxito!")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Gracias por tu compra", substring = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver al Inicio")
            .assertIsDisplayed()
    }

    @Test
    fun al_hacer_click_navega_al_home() {
        composeTestRule.setContent {
            ConfirmationScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Volver al Inicio").performClick()

        verify {
            navController.navigate(
                any<String>(),
                any<NavOptionsBuilder.() -> Unit>())
        }
    }
}