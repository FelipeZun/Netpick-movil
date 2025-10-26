package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailViewModel
import com.example.netpick_movil.viewmodel.ProductDetailViewModelFactory

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    modifier: Modifier = Modifier,
    favoritesViewModel: FavoritesViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val productDetailViewModel: ProductDetailViewModel = viewModel(
        factory = ProductDetailViewModelFactory(
            savedStateHandle = remember {
                SavedStateHandle(mapOf("productId" to productId))
            },
            favoritesViewModel = favoritesViewModel
        )
    )

    val uiState by productDetailViewModel.uiState.collectAsState()
    val product = uiState.product

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
                    IconButton(onClick = { productDetailViewModel.onFavoriteClicked() }) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (uiState.isFavorite) Color.Red else Color.Gray
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
                    IconButton(onClick = { productDetailViewModel.onQuantityChanged(uiState.quantity - 1) }) {
                        Icon(imageVector = Icons.Filled.Remove, contentDescription = "Quitar")
                    }
                    Text(text = uiState.quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { productDetailViewModel.onQuantityChanged(uiState.quantity + 1) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        product?.let {
                            cartViewModel.addToCart(it, uiState.quantity)
                        }
                    },
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
                        product?.let {
                            val totalPrice = it.precio * uiState.quantity
                            navController.navigate("purchase_success/$totalPrice")
                        }
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