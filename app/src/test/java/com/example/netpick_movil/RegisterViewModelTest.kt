package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.data.repository.SessionManager
import com.example.netpick_movil.model.RegisterRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private val authApi = mockk<AuthApi>(relaxed = true)

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(authApi, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun register_exitoso_cambiaEstado() = runTest {
        coEvery {
            authApi.register(any<RegisterRequest>())
        } returns Response.success<Unit>(Unit)

        viewModel.onEvent(RegisterFormEvent.NombreChanged("Test User"))
        viewModel.onEvent(RegisterFormEvent.CorreoChanged("test@mail.com"))
        viewModel.onEvent(RegisterFormEvent.ClaveChanged("123456"))
        viewModel.onEvent(RegisterFormEvent.TelefonoChanged("99999999"))

        viewModel.onEvent(RegisterFormEvent.Submit)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue("El registro debería ser exitoso", state.registroExitoso)
        assertFalse("Ya no debería estar cargando", state.cargando)
    }

    @Test
    fun register_errorServidor_muestraMensaje() = runTest {
        val errorBody = mockk<ResponseBody>(relaxed = true)

        coEvery {
            authApi.register(any<RegisterRequest>())
        } returns Response.error(400, errorBody)

        viewModel.onEvent(RegisterFormEvent.NombreChanged("Juan"))
        viewModel.onEvent(RegisterFormEvent.Submit)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.registroExitoso)
        assertTrue(state.error?.contains("Error del servidor") == true || state.error != null)
    }

    @Test
    fun register_excepcion_muestraError() = runTest {
        val errorMsg = "Fallo de red"
        coEvery {
            authApi.register(any<RegisterRequest>())
        } throws RuntimeException(errorMsg)

        viewModel.onEvent(RegisterFormEvent.Submit)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(errorMsg, state.error)
        assertFalse(state.cargando)
    }

    @Test
    fun onEvent_actualizaCampos() = runTest {
        viewModel.onEvent(RegisterFormEvent.NombreChanged("Ana"))
        viewModel.onEvent(RegisterFormEvent.CorreoChanged("ana@test.com"))

        val state = viewModel.uiState.value
        assertEquals("Ana", state.nombre)
        assertEquals("ana@test.com", state.correo)
    }
}