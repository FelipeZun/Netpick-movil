    package com.example.netpick_movil.ui.screen

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import coil.compose.AsyncImage
    import com.example.netpick_movil.model.Producto
    import com.example.netpick_movil.viewmodel.HomeViewModel

    @Composable
    fun HomeScreen(
        navController: NavController,
        modifier: Modifier = Modifier,
        viewModel: HomeViewModel = viewModel()
    ) {
        val uiState by viewModel.uiState.collectAsState()

        Column(modifier = modifier.fillMaxSize()) {
            // Top Bar
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    label = { Text("Buscar productos") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { navController.navigate(Screen.Login.route) }) {
                        Text("Iniciar Sesión")
                    }
                    Button(onClick = { navController.navigate(Screen.UserForm.route) }) {
                        Text("Crear Cuenta")
                    }
                }
            }

            // Product Catalog
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.productosFiltrados) { product ->
                    ProductCard(navController = navController, product = product)
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
                navController.navigate(Screen.ProductDetail.createRoute(product.id))
            }
        ) {
            Column {
                AsyncImage(
                    model = product.imageUrls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(128.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = product.nombre, fontWeight = FontWeight.Bold)
                    Text(text = "$${product.precio}")
                }
            }
        }
    }
