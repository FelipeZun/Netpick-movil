package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.netpick_movil.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mis Favoritos",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${uiState.favoriteProducts.size} productos guardados",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiState.favoriteProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No tienes favoritos aún",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Explorar productos")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val safeProducts = uiState.favoriteProducts.filter { it.idProducto != null }

                items(safeProducts) { product ->
                    FavoriteProductCard(
                        navController = navController,
                        product = product,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    navController: NavController,
    product: Producto,
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    val productId = product.idProducto

    if (productId == null) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("product_detail/$productId")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = when (val imageLink = product.linkImagen) {
                is String -> imageLink
                is List<*> -> imageLink.firstOrNull()?.toString()
                else -> "https://via.placeholder.com/80"
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = product.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre ?: "Producto Desconocido",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                val price = product.precio
                val formattedPrice = String.format("$%d", price)

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { viewModel.toggleFavorite(product) },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text("Quitar")
                }
            }
        }
    }
}
