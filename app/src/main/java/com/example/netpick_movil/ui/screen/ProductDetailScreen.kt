package com.example.netpick_movil.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.viewmodel.ProductDetailViewModel
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    viewModel: ProductDetailViewModel,
    cartViewModel: CartViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()

    val product = uiState.product
    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

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

    val isFavorite = favoritesUiState.favoriteProducts.any {
        it.idProducto == product.idProducto
    }

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        label = "FavoriteIconScale"
    )

    val imageUrl = product.linkImagen ?: "https://via.placeholder.com/300"

    Column(modifier = modifier.fillMaxSize()) {

        AsyncImage(
            model = imageUrl,
            contentDescription = product.nombre ?: "Producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.nombre ?: "Producto",
                    style = MaterialTheme.typography.headlineSmall
                )

                Row {
                    IconButton(onClick = {
                        favoritesViewModel.toggleFavorite(product)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.scale(scale)
                        )
                    }

                    IconButton(onClick = { /* compartir */ }) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$${product.precio}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { cartViewModel.addToCart(product, 1) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir 1 Unidad")
                }

                Button(
                    onClick = { navController.navigate(Screen.Confirmation.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comprar ahora")
                }
            }
        }
    }
}
