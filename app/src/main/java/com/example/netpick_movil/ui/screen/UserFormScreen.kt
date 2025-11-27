package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.netpick_movil.data.local.AppDatabase
import com.example.netpick_movil.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(
    modifier: Modifier = Modifier,
    viewModel: UsuarioViewModel = getUsuarioViewModel(),
    onRegisterSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val errores = uiState.errores
    val scrollState = rememberScrollState()

    LaunchedEffect(errores.registro) {
        if (errores.registro?.contains("éxito", ignoreCase = true) == true) {
            kotlinx.coroutines.delay(2000)
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Netpick Registro") })
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onEvent(UsuarioFormEvent.NombreChanged(it)) },
                label = { Text("Nombre") },
                isError = errores.nombre != null,
                modifier = Modifier.fillMaxWidth()
            )
            errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { viewModel.onEvent(UsuarioFormEvent.CorreoChanged(it)) },
                label = { Text("Correo") },
                isError = errores.correo != null,
                modifier = Modifier.fillMaxWidth()
            )
            errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(
                value = uiState.clave,
                onValueChange = { viewModel.onEvent(UsuarioFormEvent.ClaveChanged(it)) },
                label = { Text("Contraseña") },
                isError = errores.clave != null,
                modifier = Modifier.fillMaxWidth()
            )
            errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(
                value = uiState.telefono,
                onValueChange = { viewModel.onEvent(UsuarioFormEvent.TelefonoChanged(it)) },
                label = { Text("Teléfono") },
                isError = errores.telefono != null,
                modifier = Modifier.fillMaxWidth()
            )
            errores.telefono?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = { viewModel.onEvent(UsuarioFormEvent.DireccionChanged(it)) },
                label = { Text("Dirección (Opcional)") },
                isError = errores.direccion != null,
                modifier = Modifier.fillMaxWidth()
            )
            errores.direccion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.aceptaTerminos,
                    onCheckedChange = { viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(it)) }
                )
                Text("Acepto los términos y condiciones")
            }
            errores.aceptaTerminos?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            errores.registro?.let { mensaje ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = mensaje,
                    color = if (mensaje.contains("éxito", ignoreCase = true)) Color.Green else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
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
}

@Composable
fun getUsuarioViewModel(): UsuarioViewModel {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getInstance(context).usuarioDao() }
    val factory = remember { UsuarioViewModelFactory(dao) }
    return viewModel(factory = factory)
}