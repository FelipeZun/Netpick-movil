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
        // Mock data
        _uiState.update { it.copy(productos = getMockProductos()) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        // TODO: Implement search logic
    }

    private fun getMockProductos(): List<Producto> {
        return listOf(
            Producto("1", "Teléfono 1", 1000.0, "https://via.placeholder.com/150"),
            Producto("2", "Teléfono 2", 1200.0, "https://via.placeholder.com/150"),
            Producto("3", "Teléfono 3", 1300.0, "https://via.placeholder.com/150"),
            Producto("4", "Teléfono 4", 1400.0, "https://via.placeholder.com/150"),
            Producto("5", "Teléfono 5", 1500.0, "https://via.placeholder.com/150"),
        )
    }
}
