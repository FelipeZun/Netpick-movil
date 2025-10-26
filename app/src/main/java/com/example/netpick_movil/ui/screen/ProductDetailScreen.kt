package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product

    val isFavorite = favoritesViewModel.isFavorite(productId)

    if (product == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = product.imageUrls.firstOrNull(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = { product?.let { favoritesViewModel.toggleFavorite(it) } }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement Share */ }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$${product.precio}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.onQuantityChanged(uiState.quantity - 1) }) {
                        Icon(imageVector = Icons.Filled.Remove, contentDescription = "Quitar")
                    }
                    Text(text = uiState.quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { viewModel.onQuantityChanged(uiState.quantity + 1) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { product?.let { cartViewModel.addToCart(it, uiState.quantity) } },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Añadir al carrito"
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Añadir al carrito")
                }

                Button(
                    onClick = {
                        // Navega directamente a la confirmación
                        navController.navigate(Screen.Confirmation.route)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comprar ahora")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}