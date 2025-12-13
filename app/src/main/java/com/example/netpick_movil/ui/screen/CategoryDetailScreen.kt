package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.viewmodel.CategoryViewModel
import com.example.netpick_movil.viewmodel.CategoryViewModelFactory

@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String,
    viewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepository(RetrofitClient.apiService))
    )
) {

    LaunchedEffect(categoryId) {
        viewModel.loadProductos(categoryId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = categoryName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                val productos = uiState.productos
                if (productos.isEmpty()) {
                    Text(
                        text = "No hay productos en esta categoría",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
}