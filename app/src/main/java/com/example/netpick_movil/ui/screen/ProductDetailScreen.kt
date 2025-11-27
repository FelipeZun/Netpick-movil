package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    viewModel: ProductDetailViewModel,
    cartViewModel: CartViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = productId) {
        viewModel.loadProduct(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val product: Producto? = uiState.product
    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage
    val quantity = uiState.quantity

    when {
        isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }
        errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            return
        }
        product == null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Producto no encontrado")
            }
            return
        }
    }

    val imageUrl = (product.linkImagen as? List<String>)?.firstOrNull() ?: product.linkImagen as? String ?: "https://via.placeholder.com/300"

    Column(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = product.nombre ?: "Producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.nombre ?: "Producto Desconocido",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    val favourite = favoritesViewModel.isFavorite(product.idProducto)
                    IconButton(onClick = {
                        if (product.idProducto != null) favoritesViewModel.toggleFavorite(product)
                    }) {
                        Icon(
                            imageVector = if (favourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = { /* compartir */ }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val displayPrice = String.format("$%.2f", product.precio?.toDouble() ?: 0.0)
                Text(text = displayPrice, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.onQuantityChanged(quantity - 1) }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Quitar")
                    }
                    Text(text = quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { viewModel.onQuantityChanged(quantity + 1) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { cartViewModel.addToCart(product, quantity) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir")
                }

                Button(
                    onClick = { navController.navigate(Screen.Confirmation.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comprar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.descripcion ?: "Sin descripción disponible.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}