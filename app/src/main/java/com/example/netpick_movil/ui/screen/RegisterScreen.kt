package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.netpick_movil.data.repository.SessionManager
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.viewmodel.RegisterFormEvent
import com.example.netpick_movil.viewmodel.RegisterViewModel
import com.example.netpick_movil.viewmodel.RegisterViewModelFactory

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            authApi = RetrofitClient.authApi,
            sessionManager = SessionManager()


        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            navController.navigate("login_route") {
                popUpTo("user_form") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.onEvent(RegisterFormEvent.NombreChanged(it)) },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.correo,
            onValueChange = { viewModel.onEvent(RegisterFormEvent.CorreoChanged(it)) },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.telefono,
            onValueChange = { viewModel.onEvent(RegisterFormEvent.TelefonoChanged(it)) },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.clave,
            onValueChange = { viewModel.onEvent(RegisterFormEvent.ClaveChanged(it)) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        uiState.error?.let {
            Spacer(Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onEvent(RegisterFormEvent.Submit) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.cargando) {
                CircularProgressIndicator(Modifier.size(24.dp))
            } else {
                Text("Registrarse")
            }
        }
    }
}
