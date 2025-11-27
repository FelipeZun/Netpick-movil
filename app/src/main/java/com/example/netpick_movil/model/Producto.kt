package com.example.netpick_movil.model;

data class Producto(
    val idProducto: Int,
    val nombre: String?,
    val descripcion: String?,
    val precio: Int,
    val stock: Int,
    val linkImagen: String?,
    val categoria: Categoria?

)