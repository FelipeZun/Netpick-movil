package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.netpick_movil.data.local.AppDatabase
import com.example.netpick_movil.viewmodel.LoginFormEvent
import com.example.netpick_movil.viewmodel.LoginViewModel
import com.example.netpick_movil.viewmodel.LoginViewModelFactory

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = loginViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = uiState.correo,
            onValueChange = { viewModel.onEvent(LoginFormEvent.CorreoChanged(it)) },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = uiState.clave,
            onValueChange = { viewModel.onEvent(LoginFormEvent.ClaveChanged(it)) },
            label = { Text("Clave") },
            modifier = Modifier.fillMaxWidth()
        )

        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onEvent(LoginFormEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.cargando
        ) {
            if (uiState.cargando) {
                CircularProgressIndicator()
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Screen.UserForm.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }
    }
}

@Composable
fun loginViewModel(): LoginViewModel {
    val database = AppDatabase.getInstance(androidx.compose.ui.platform.LocalContext.current)
    val factory = LoginViewModelFactory(database.usuarioDao())
    return viewModel(factory = factory)
}