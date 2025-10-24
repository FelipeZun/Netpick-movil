package com.example.netpick_movil.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProductDetailState(
    val product: Producto? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false
)

class ProductDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    init {
        val product = getMockProductos().find { it.id == productId }
        _uiState.update { it.copy(product = product) }
    }

    private fun getMockProductos(): List<Producto> {
        return listOf(
            Producto(
                id = "1", 
                nombre = "Celular Samsung Galaxy A73", 
                precio = 450.000,
                imageUrls = listOf("https://i.blogs.es/869f92/samsung-galaxy-a73-1/650_1200.jpg", "https://i.blogs.es/869f92/samsung-galaxy-a73-1/650_1200.jpg"),
                description = "Experimenta un rendimiento increíble con el procesador Snapdragon 778G 5G. La cámara de 108 MP captura detalles asombrosos, y la pantalla Super AMOLED+ de 120 Hz ofrece una visualización fluida y vibrante."
            ),
            Producto(
                id = "2", 
                nombre = "Computador de Escritorio 'Gamer'", 
                precio = 180.000,
                imageUrls = listOf("https://http2.mlstatic.com/D_NQ_NP_687797-MLA78325165728_082024-O.webp", "https://http2.mlstatic.com/D_NQ_NP_687797-MLA78325165728_082024-O.webp"),
                description = "Domina el campo de batalla con este PC gamer. Equipado con un procesador de última generación, una tarjeta gráfica potente y memoria RAM de alta velocidad para una experiencia de juego sin igual."
            ),
            Producto(
                id = "3", 
                nombre = "Monitor MSI Optix G241", 
                precio = 250.000,
                imageUrls = listOf("https://tse4.mm.bing.net/th/id/OIP.U3OXUC0wlmz2yq7IZECglwHaHa?rs=1&pid=ImgDetMain&o=7&rm="),
                description = "Visualiza tu victoria con el monitor de juegos MSI Optix G241. Equipado con una frecuencia de actualización de 144 Hz y un panel IPS con tiempo de respuesta de 1 ms, te dará la ventaja competitiva que necesitas."
            )
        )
    }

    fun onFavoriteClicked() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun onQuantityChanged(newQuantity: Int) {
        if (newQuantity > 0) {
            _uiState.update { it.copy(quantity = newQuantity) }
        }
    }
}
