package com.example.netpick_movil.data.remote.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.netpick_movil.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun registrar (usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login (correo: String, clave: String): Usuario?
}
