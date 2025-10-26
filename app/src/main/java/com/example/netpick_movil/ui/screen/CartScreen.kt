package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalItems = uiState.cartItems.sumOf { it.quantity }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mi Carrito",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$totalItems productos en el carrito",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiState.cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(Screen.Home.route) }) {
                    Text("Seguir comprando")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.cartItems) { cartItem ->
                    CartItemCard(
                        product = cartItem.product,
                        quantity = cartItem.quantity,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateQuantity(cartItem.product.id, newQuantity)
                        },
                        onRemove = {
                            viewModel.removeFromCart(cartItem.product.id)
                        },
                        navController = navController
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total:", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$${uiState.totalPrice}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.clearCart()
                            navController.navigate(Screen.Confirmation.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Comprar ahora")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    product: Producto,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            navController.navigate(Screen.ProductDetail.createRoute(product.id))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrls.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .weight(0.3f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(start = 16.dp)
            ) {
                Text(text = product.nombre, fontWeight = FontWeight.Bold)
                Text(text = "$${product.precio}")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onQuantityChange(quantity - 1) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                        }
                        Text(text = quantity.toString())
                        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir")
                        }
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}