package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.MainDispatcherRule
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class UsuarioViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val mockDao = mockk<UsuarioDao>(relaxed = true)
    private val viewModel = UsuarioViewModel(mockDao)

    @Test
    fun `validacion falla si los campos estan vacios`() {
        viewModel.onEvent(UsuarioFormEvent.Submit)
        val errors = viewModel.uiState.value.errores
        assertNotNull(errors.nombre)
        assertNotNull(errors.direccion)
    }

    @Test
    fun `registro exitoso llama al DAO`() {
        viewModel.onEvent(UsuarioFormEvent.NombreChanged("Gabo"))
        viewModel.onEvent(UsuarioFormEvent.CorreoChanged("gabo@test.com"))
        viewModel.onEvent(UsuarioFormEvent.ClaveChanged("123456"))
        viewModel.onEvent(UsuarioFormEvent.DireccionChanged("Calle Falsa 123"))
        viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(true))
        viewModel.onEvent(UsuarioFormEvent.Submit)
        coVerify { mockDao.registrar(any()) }
        assertEquals("¡Registro exitoso!", viewModel.uiState.value.errores.registro)
    }
}