package com.example.netpick_movil.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.netpick_movil.viewmodel.AuthViewModel
import com.example.netpick_movil.viewmodel.LoginFormEvent
import com.example.netpick_movil.viewmodel.LoginUIState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<AuthViewModel>(relaxed = true)
    private val uiStateFlow = MutableStateFlow(LoginUIState())

    private fun setupScreen() {
        every { viewModel.uiState } returns uiStateFlow.asStateFlow()

        composeTestRule.setContent {
            AuthScreen(
                navController = navController,
                authViewModel = viewModel
            )
        }
    }

    @Test
    fun elementos_iniciales_se_muestran_correctamente() {
        setupScreen()
        composeTestRule.onNode(
            hasText("Iniciar Sesión") and !hasClickAction()
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNode(
            hasText("Iniciar Sesión") and hasClickAction()
        ).assertIsDisplayed().assertIsEnabled()
    }

    @Test
    fun escribir_texto_dispara_eventos_viewmodel() {
        setupScreen()
        composeTestRule.onNodeWithText("Correo Electrónico").performTextInput("test@mail.com")
        verify { viewModel.onEvent(LoginFormEvent.CorreoChanged("test@mail.com")) }

        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        verify { viewModel.onEvent(LoginFormEvent.ClaveChanged("123456")) }
    }

    @Test
    fun boton_iniciar_sesion_dispara_submit() {
        setupScreen()
        composeTestRule.onNodeWithText("Correo Electrónico").performTextInput("a")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("b")
        composeTestRule.onNode(hasText("Iniciar Sesión") and hasClickAction()).performClick()

        verify { viewModel.onEvent(LoginFormEvent.Submit) }
    }

    @Test
    fun login_exitoso_navega_a_home() {
        setupScreen()

        uiStateFlow.value = LoginUIState(loginExitoso = true)

        composeTestRule.waitForIdle()

        verify {
            navController.navigate("home", any<NavOptionsBuilder.() -> Unit>())
        }
    }
}