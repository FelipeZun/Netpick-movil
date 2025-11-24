package com.example.netpick_movil.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.netpick_movil.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initLoadsCorrectProductById() {
        val savedStateHandle = SavedStateHandle(mapOf("productId" to "1"))
        val viewModel = ProductDetailViewModel(savedStateHandle)

        val state = viewModel.uiState.value
        assertEquals("1", state.product?.id)
        assertEquals("Celular Samsung Galaxy A73", state.product?.nombre)
    }

    @Test
    fun onQuantityChangedUpdatesValueIfValid() {
        val savedStateHandle = SavedStateHandle(mapOf("productId" to "1"))
        val viewModel = ProductDetailViewModel(savedStateHandle)

        viewModel.onQuantityChanged(5)
        assertEquals(5, viewModel.uiState.value.quantity)

        viewModel.onQuantityChanged(-1)
        assertEquals(5, viewModel.uiState.value.quantity)
    }
}