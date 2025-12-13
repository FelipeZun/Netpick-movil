package com.example.netpick_movil.data.remote.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.netpick_movil.data.local.AppDatabase
import com.example.netpick_movil.data.remote.dao.UsuarioDao
import com.example.netpick_movil.model.Usuario
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsuarioDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        usuarioDao = db.usuarioDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun registrar_y_buscar_usuario() = runBlocking {
        val usuario = Usuario(
            id = 1,
            nombre = "Gabo",
            correo = "gabo@test.com",
            clave = "123456",
            direccion = "Calle Falsa 123",
            telefono = "987654321",
            rol = "cliente"
        )

        usuarioDao.registrar(usuario)

        val encontrado = usuarioDao.login("gabo@test.com", "123456")

        assertNotNull(encontrado)
        assertEquals("Gabo", encontrado?.nombre)
        assertEquals("cliente", encontrado?.rol)
    }
}