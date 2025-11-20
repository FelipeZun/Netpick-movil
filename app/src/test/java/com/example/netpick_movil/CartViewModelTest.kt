package com.example.netpick_movil.viewmodel

import com.example.netpick_movil.CoroutinesTestExtension
import com.example.netpick_movil.model.Producto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class CartViewModelTest : BehaviorSpec({

    extension(CoroutinesTestExtension())

    val productoPrueba = Producto(
        id = "1",
        nombre = "Test Phone",
        precio = 1000.0,
        imageUrls = emptyList(),
        description = "Desc"
    )

    Given("Un CartViewModel vacío") {
        val viewModel = CartViewModel()

        When("Añado un producto con cantidad 2") {
            viewModel.addToCart(productoPrueba, 2)

            Then("El carrito debe tener 1 item") {
                viewModel.uiState.value.cartItems shouldHaveSize 1
            }

            Then("El precio total debe ser 2000.0") {
                viewModel.uiState.value.totalPrice shouldBe 2000.0
            }
        }

        When("Limpio el carrito") {
            viewModel.clearCart()

            Then("El carrito debe estar vacío") {
                viewModel.uiState.value.cartItems shouldHaveSize 0
                viewModel.uiState.value.totalPrice shouldBe 0.0
            }
        }
    }
})