<<<<<<< HEAD
package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUIState(
    val searchQuery: String = "",
    val productos: List<Producto> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(productos = getMockProductos()) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        // TODO: Implement search logic
    }

    private fun getMockProductos(): List<Producto> {
        return listOf(
            Producto(
                id = "1", 
                nombre = "Celular Samsung Galaxy A73", 
                precio = 450.000,
                imageUrls = listOf("https://picsum.photos/seed/samsung/800/600", "https://picsum.photos/seed/samsung2/800/600"), 
                description = "Experimenta un rendimiento increíble con el procesador Snapdragon 778G 5G. La cámara de 108 MP captura detalles asombrosos, y la pantalla Super AMOLED+ de 120 Hz ofrece una visualización fluida y vibrante."
            ),
            Producto(
                id = "2", 
                nombre = "Computador de Escritorio 'Gamer'", 
                precio = 180.000,
                imageUrls = listOf("https://picsum.photos/seed/gamerpc/800/600"),
                description = "Domina el campo de batalla con este PC gamer. Equipado con un procesador de última generación, una tarjeta gráfica potente y memoria RAM de alta velocidad para una experiencia de juego sin igual."
            ),
            Producto(
                id = "3", 
                nombre = "Monitor MSI Optix G241", 
                precio = 250.000,
                imageUrls = listOf("https://picsum.photos/seed/msimonitor/800/600"), 
                description = "Visualiza tu victoria con el monitor de juegos MSI Optix G241. Equipado con una frecuencia de actualización de 144 Hz y un panel IPS con tiempo de respuesta de 1 ms, te dará la ventaja competitiva que necesitas."
            )
        )
    }
}
=======
package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUIState(
    val searchQuery: String = "",
    val productos: List<Producto> = emptyList(),
    val productosFiltrados: List<Producto> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    private val productosOriginales = getMockProductos()

    init {
        _uiState.update {
            it.copy(
                productos = productosOriginales,
                productosFiltrados = productosOriginales
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            val productosFiltrados = if (query.isBlank()) {
                productosOriginales
            } else {
                filtrarProductos(query, productosOriginales)
            }

            currentState.copy(
                searchQuery = query,
                productosFiltrados = productosFiltrados
            )
        }
    }

    private fun filtrarProductos(query: String, productos: List<Producto>): List<Producto> {
        val queryLowercase = query.lowercase().trim()

        return productos.filter { producto ->
            producto.nombre.lowercase().contains(queryLowercase) ||
                    producto.description.lowercase().contains(queryLowercase)
        }
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
                imageUrls = listOf("https://http2.mlstatic.com/D_NQ_NP_687797-MLA78325165728_082024-O.webp"),
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
}
>>>>>>> origin/master
