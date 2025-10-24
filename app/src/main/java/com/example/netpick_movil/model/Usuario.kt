<<<<<<< HEAD
package com.example.netpick_movil.model

data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val direccion: String
)
=======
package com.example.netpick_movil.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String
)
>>>>>>> origin/master
