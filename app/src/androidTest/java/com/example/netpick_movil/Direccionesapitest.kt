package com.example.netpick_movil

import com.example.netpick_movil.data.remote.api.RetrofitClient
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DireccionesApiTest {

    @Test
    fun testDireccionesApi() = runBlocking {
        val response = RetrofitClient.apiService.getDireccion(1)

        assertTrue(response.isSuccessful)
        val dir = response.body()!!

        assertEquals(1, dir.idDireccion)
        assertEquals("Santiago", dir.comuna.nombre)
        assertEquals("Región Metropolitana", dir.comuna.region.nombre)
    }
}
