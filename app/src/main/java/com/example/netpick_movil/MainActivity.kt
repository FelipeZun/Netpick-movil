package com.example.netpick_movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.netpick_movil.model.Producto
import com.example.netpick_movil.ui.screen.Screen
import com.example.netpick_movil.ui.theme.NetpickMovilTheme
import com.example.netpick_movil.viewmodel.HomeViewModel
import com.example.netpick_movil.viewmodel.LoginFormEvent
import com.example.netpick_movil.viewmodel.LoginViewModel
import com.example.netpick_movil.viewmodel.UsuarioFormEvent
import com.example.netpick_movil.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetpickMovilTheme {
                NetpickApp()
            }
        }
    }
}

@Composable
fun NetpickApp() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Start at Home
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.UserForm.route) {
                UserFormScreen()
            }
        }
    }
}

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
            items(uiState.productos) { product ->
                ProductCard(product = product)
            }
        }
    }
}


@Composable
fun ProductCard(product: Producto, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            AsyncImage(
                model = product.imageUrl,
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


@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.loginSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = uiState.correo,
                onValueChange = { viewModel.onEvent(LoginFormEvent.CorreoChanged(it)) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = uiState.clave,
                onValueChange = { viewModel.onEvent(LoginFormEvent.ClaveChanged(it)) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.onEvent(LoginFormEvent.LoginClicked) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
            }

            uiState.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen")
    }
}

@Composable
fun UserFormScreen(
    modifier: Modifier = Modifier,
    viewModel: UsuarioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = uiState.nombre,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.NombreChanged(it)) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = uiState.correo,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.CorreoChanged(it)) },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = uiState.clave,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.ClaveChanged(it)) },
            label = { Text("Clave") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = uiState.direccion,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.DireccionChanged(it)) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.aceptaTerminos,
                onCheckedChange = { viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(it)) }
            )
            Text("Acepto los términos y condiciones")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.onEvent(UsuarioFormEvent.Submit) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NetpickMovilTheme {
        NetpickApp()
    }
}
