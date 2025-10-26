package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
                Button(onClick = { navController.navigate(Screen.Home.route) }) {
                    Text("Explorar productos")
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.favoriteProducts) { product ->
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
    Card(
        modifier = modifier.clickable {
            navController.navigate(Screen.ProductDetail.createRoute(product.id))
        }
    ) {
        Column {
            AsyncImage(
                model = product.imageUrls.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = product.nombre, fontWeight = FontWeight.Bold)
                Text(text = "$${product.precio}")

                Button(
                    onClick = { viewModel.toggleFavorite(product) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Quitar")
                }
            }
        }
    }
}