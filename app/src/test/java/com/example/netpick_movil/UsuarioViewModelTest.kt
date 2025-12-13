package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.data.remote.api.AuthApi
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.Usuario
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    private lateinit var viewModel: UsuarioViewModel
    private val dao: UsuarioDao = mockk(relaxed = true)
    private val authApiMock: AuthApi = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkObject(RetrofitClient)
        every { RetrofitClient.authApi } returns authApiMock

        viewModel = UsuarioViewModel(dao)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun onEvent_actualizaEstadoCorrectamente() = runTest {
        viewModel.onEvent(UsuarioFormEvent.NombreChanged("Carlos"))
        viewModel.onEvent(UsuarioFormEvent.CorreoChanged("carlos@mail.com"))
        viewModel.onEvent(UsuarioFormEvent.TelefonoChanged("12345678"))
        viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(true))

        val state = viewModel.uiState.value
        assertEquals("Carlos", state.nombre)
        assertEquals("carlos@mail.com", state.correo)
        assertEquals("12345678", state.telefono)
        assertTrue(state.aceptaTerminos)
    }

    @Test
    fun validateForm_detectaErrores() = runTest {
        viewModel.onEvent(UsuarioFormEvent.NombreChanged(""))
        viewModel.onEvent(UsuarioFormEvent.CorreoChanged("correo-invalido"))
        viewModel.onEvent(UsuarioFormEvent.ClaveChanged("123"))
        viewModel.onEvent(UsuarioFormEvent.TelefonoChanged("abc"))
        viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(false))

        viewModel.onEvent(UsuarioFormEvent.Submit)

        val errores = viewModel.uiState.value.errores
        assertNotNull(errores.nombre)
        assertNotNull(errores.correo)
        assertNotNull(errores.clave)
        assertNotNull(errores.telefono)
        assertNotNull(errores.aceptaTerminos)
    }

    @Test
    fun registrarUsuario_exitoso_actualizaEstado() = runTest {
        viewModel.onEvent(UsuarioFormEvent.NombreChanged("Carlos"))
        viewModel.onEvent(UsuarioFormEvent.CorreoChanged("carlos@valid.com"))
        viewModel.onEvent(UsuarioFormEvent.ClaveChanged("passwordSeguro"))
        viewModel.onEvent(UsuarioFormEvent.TelefonoChanged("98765432"))
        viewModel.onEvent(UsuarioFormEvent.DireccionChanged("Calle Falsa 123"))
        viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(true))

        coEvery {
            authApiMock.register(any())
        } returns Response.success<Unit>(Unit)

        viewModel.onEvent(UsuarioFormEvent.Submit)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("¡Cuenta creada con éxito!", state.errores.registro)
    }

    @Test
    fun registrarUsuario_errorApi_actualizaMensaje() = runTest {
        viewModel.onEvent(UsuarioFormEvent.NombreChanged("Carlos"))
        viewModel.onEvent(UsuarioFormEvent.CorreoChanged("carlos@valid.com"))
        viewModel.onEvent(UsuarioFormEvent.ClaveChanged("passwordSeguro"))
        viewModel.onEvent(UsuarioFormEvent.TelefonoChanged("98765432"))
        viewModel.onEvent(UsuarioFormEvent.DireccionChanged("Calle Falsa 123"))
        viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(true))

        val errorBody = mockk<ResponseBody>(relaxed = true)
        coEvery { authApiMock.register(any()) } returns Response.error(500, errorBody)

        viewModel.onEvent(UsuarioFormEvent.Submit)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.errores.registro?.contains("Error 500") == true)
    }

    @Test
    fun cargarUsuarioPorCorreo_encuentraUsuario() = runTest {
        val correo = "test@mail.com"
        val userMock = Usuario(1, "Test", correo, "123", "Calle 1", "175675634", "1")

        coEvery { dao.buscarPorCorreo(correo) } returns userMock

        viewModel.cargarUsuarioPorCorreo(correo)
        advanceUntilIdle()

        val user = viewModel.usuario.value
        assertNotNull(user)
        assertEquals(correo, user?.correo)
    }
}