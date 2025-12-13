package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.data.repository.AuthRepository
import com.example.netpick_movil.data.repository.Result
import com.example.netpick_movil.model.LoginRequest
import com.example.netpick_movil.model.UserSession
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
    private val repository: AuthRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { repository.getLoggedInUser() } returns null
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login con credenciales correctas cambia estado a loginExitoso`() = runTest {
        val correoTest = "hola@correo.com"
        val claveTest = "123456"

        val dataSimulada = mockk<UserSession>(relaxed = true)

        coEvery { repository.login(correoTest, claveTest) } returns Result.Success(dataSimulada)

        viewModel.onEvent(LoginFormEvent.CorreoChanged(correoTest))
        viewModel.onEvent(LoginFormEvent.ClaveChanged(claveTest))

        viewModel.onEvent(LoginFormEvent.Submit)

        advanceUntilIdle()

        assertTrue("El login debería ser exitoso", viewModel.uiState.value.loginExitoso)
        assertFalse("No debería estar cargando", viewModel.uiState.value.cargando)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `login con campos vacios genera error de validacion`() = runTest {
        viewModel.onEvent(LoginFormEvent.Submit)
        val errorActual = viewModel.uiState.value.error
        assertEquals("Correo y clave son obligatorios.", errorActual)
    }

    @Test
    fun `login fallido muestra mensaje de error desde repositorio`() = runTest {
        val correo = "error@duoc.cl"
        val clave = "mala"
        val errorMsg = "Credenciales incorrectas"
        coEvery { repository.login(correo, clave) } returns Result.Error(Exception(errorMsg))
        viewModel.onEvent(LoginFormEvent.CorreoChanged(correo))
        viewModel.onEvent(LoginFormEvent.ClaveChanged(clave))
        viewModel.onEvent(LoginFormEvent.Submit)

        advanceUntilIdle()
        assertTrue("Debería haber un error", viewModel.uiState.value.error?.contains(errorMsg) == true)
        assertFalse("Login no debería ser exitoso", viewModel.uiState.value.loginExitoso)
    }
}