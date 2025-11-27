package com.example.netpick_movil.model

data class Direccion(
    val idDireccion: Int,
    val direccion: String,
    val codigoPostal: String,
    val pais: String,
    val usuario: UsuarioDireccion,
    val comuna: Comuna
)