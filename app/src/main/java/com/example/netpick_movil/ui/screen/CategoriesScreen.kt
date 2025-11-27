package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.HomeViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,

    viewModel: HomeViewModel = getHomeViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf("Todas") }

    val dynamicCategories = remember(uiState.productos) {
        val catNames = uiState.productos
            .mapNotNull { it.categoria?.nombre }
            .distinct()
            .sorted()
        listOf("Todas") + catNames
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                dynamicCategories.take(4).forEach { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Buscar producto") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { viewModel.obtenerProductos() }, modifier = Modifier.padding(16.dp)) {
                    Text("Reintentar")
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    val categoryFiltered = if (selectedCategory == "Todas") {
                        uiState.productosFiltrados
                    } else {
                        uiState.productosFiltrados.filter {
                            it.categoria?.nombre.equals(selectedCategory, ignoreCase = true)
                        }
                    }

                    items(categoryFiltered) { product ->
                        ProductCard(navController = navController, product = product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(navController: NavController, product: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Imagen del producto
            AsyncImage(
                model = product.linkImagen ?: "https://via.placeholder.com/150",
                contentDescription = product.nombre,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                onError = {
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.nombre ?: "Sin nombre",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Text(
                text = "$ ${product.precio}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            product.categoria?.nombre?.let { catNombre ->
                Text(
                    text = catNombre,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun getHomeViewModel(): HomeViewModel {
    val factory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(RetrofitClient.apiService) as T
            }
        }
    }
    return viewModel(factory = factory)
}