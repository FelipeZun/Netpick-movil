package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.netpick_movil.model.Usuario
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val onLogoutMock = mockk<() -> Unit>(relaxed = true)

    @Test
    fun muestra_mensaje_error_y_boton_login_cuando_usuario_es_nulo() {
        composeTestRule.setContent {
            ProfileScreen(
                navController = navController,
                usuario = null,
                onLogout = onLogoutMock
            )
        }

        composeTestRule.onNodeWithText("No se encontró información del usuario.")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Volver al Login").performClick()

        verify { onLogoutMock() }
    }

    @Test
    fun muestra_informacion_del_usuario_y_boton_logout() {
        val usuarioPrueba = Usuario(
            id = 1,
            nombre = "Juan Pérez",
            correo = "juan@test.com",
            clave = "password123",
            direccion = "Direccion",
            telefono = "+56912345678",
            rol = "Cliente"
        )

        composeTestRule.setContent {
            ProfileScreen(
                navController = navController,
                usuario = usuarioPrueba,
                onLogout = onLogoutMock
            )
        }

        composeTestRule.onNodeWithText("Mi Perfil").assertIsDisplayed()

        composeTestRule.onNodeWithText("Juan Pérez").assertIsDisplayed()
        composeTestRule.onNodeWithText("juan@test.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("+56912345678").assertIsDisplayed()
        composeTestRule.onNodeWithText("Direccion").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cerrar Sesión").performClick()

        verify { onLogoutMock() }
    }
}