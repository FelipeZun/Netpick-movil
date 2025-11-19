package com.example.netpick_movil.model

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("imageUrls") val imageUrls: List<String>,
    @SerializedName("description") val description: String
)