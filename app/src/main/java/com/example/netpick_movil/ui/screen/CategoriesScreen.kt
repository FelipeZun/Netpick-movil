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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.model.Categoria
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.viewmodel.CategoryViewModel
import com.example.netpick_movil.viewmodel.CategoryViewModelFactory
import com.example.netpick_movil.viewmodel.HomeViewModel

@Composable
fun CategoriesScreen(navController: NavController) {
    println("DEBUG: Entré a CategoriesScreen")
    val repository = remember { CategoryRepository(RetrofitClient.apiService) }
    val factory = remember { CategoryViewModelFactory(repository) }
    val viewModel: CategoryViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategorias()
        println("DEBUG_CATEGORIES: ${viewModel.uiState.value.categorias}")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (uiState.error != null) {
            Text(
                text = "Error: ${uiState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.categorias) { categoria ->
                    CategoryCard(
                        category = categoria,
                        onClick = {
                            val encodedName = URLEncoder.encode(
                                categoria.nombre ?: "Categoría",
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("category/${categoria.idCategoria}?name=$encodedName")
                        }
                    )
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
                navController.navigate("product_detail/${product.idProducto}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
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

@Composable
fun CategoryCard(category: Categoria, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = category.nombre ?: "Categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}