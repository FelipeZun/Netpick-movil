package com.example.netpick_movil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.netpick_movil.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(product: Producto, quantity: Int) {
        _uiState.update { currentState ->
            val cartItems = currentState.cartItems.toMutableList()
            val existingItem = cartItems.find { it.product.id == product.id }

            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                val itemIndex = cartItems.indexOf(existingItem)
                cartItems[itemIndex] = updatedItem
            } else {
                cartItems.add(CartItem(product = product, quantity = quantity))
            }

            val newTotalPrice = calculateTotalPrice(cartItems)
            currentState.copy(cartItems = cartItems, totalPrice = newTotalPrice)
        }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        _uiState.update { currentState ->
            val cartItems = currentState.cartItems.toMutableList()
            val existingItem = cartItems.find { it.product.id == productId }

            if (existingItem != null) {
                if (newQuantity > 0) {
                    val updatedItem = existingItem.copy(quantity = newQuantity)
                    val itemIndex = cartItems.indexOf(existingItem)
                    cartItems[itemIndex] = updatedItem
                } else {
                    cartItems.remove(existingItem)
                }
            }

            val newTotalPrice = calculateTotalPrice(cartItems)
            currentState.copy(cartItems = cartItems, totalPrice = newTotalPrice)
        }
    }

    fun removeFromCart(productId: String) {
        _uiState.update { currentState ->
            val cartItems = currentState.cartItems.toMutableList()
            val itemToRemove = cartItems.find { it.product.id == productId }
            if (itemToRemove != null) {
                cartItems.remove(itemToRemove)
            }

            val newTotalPrice = calculateTotalPrice(cartItems)
            currentState.copy(cartItems = cartItems, totalPrice = newTotalPrice)
        }
    }

    private fun calculateTotalPrice(cartItems: List<CartItem>): Double {
        return cartItems.sumOf { it.product.precio * it.quantity }
    }
}

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)

data class CartItem(
    val product: com.example.netpick_movil.model.Producto,
    val quantity: Int = 1
)