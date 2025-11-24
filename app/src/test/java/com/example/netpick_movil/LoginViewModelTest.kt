package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.MainDispatcherRule
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.Usuario
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockDao = mockk<UsuarioDao>()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun loginSuccessUpdatesStateWithUser() {
        val fakeUser = Usuario(nombre = "Gabo", correo = "gabo@test.com", clave = "123456", direccion = "Casa")
        coEvery { mockDao.login("gabo@test.com", "123456") } returns fakeUser

        viewModel = LoginViewModel(mockDao)

        viewModel.onEvent(LoginFormEvent.CorreoChanged("gabo@test.com"))
        viewModel.onEvent(LoginFormEvent.ClaveChanged("123456"))
        viewModel.onEvent(LoginFormEvent.Submit)

        val state = viewModel.uiState.value
        assertEquals(true, state.loginExitoso)
        assertEquals(fakeUser, state.usuarioLogueado)
        assertNull(state.error)
    }

    @Test
    fun loginFailureShowsError() {
        coEvery { mockDao.login(any(), any()) } returns null

        viewModel = LoginViewModel(mockDao)

        viewModel.onEvent(LoginFormEvent.CorreoChanged("mal@test.com"))
        viewModel.onEvent(LoginFormEvent.ClaveChanged("wrongpass"))
        viewModel.onEvent(LoginFormEvent.Submit)

        val state = viewModel.uiState.value
        assertEquals(false, state.loginExitoso)
        assertEquals("Credenciales incorrectas", state.error)
    }
}