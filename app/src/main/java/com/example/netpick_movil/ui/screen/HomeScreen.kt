package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.HomeViewModel
import com.example.netpick_movil.viewmodel.HomeViewModelFactory

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val apiService = RetrofitClient.apiService

    val factory = HomeViewModelFactory(apiService = apiService)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsState()
    val productos = uiState.productosFiltrados

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Buscar productos") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        when {
            uiState.isLoading -> {
                Text("Cargando productos...", modifier = Modifier.padding(16.dp))
            }

            uiState.error != null -> {
                Text("Error al cargar: ${uiState.error}", modifier = Modifier.padding(16.dp))
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productos) { product ->
                        ProductCard(
                            navController = navController,
                            product = product
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    navController: NavController,
    product: Producto,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            navController.navigate(Screen.ProductDetail.createRoute(product.idProducto))
        }
    ) {
        Column {
            val imageUrl = product.linkImagen ?: "https://via.placeholder.com/300"

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.nombre ?: "Producto",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$${product.precio}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
