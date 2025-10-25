package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.netpick_movil.usuarioViewModel
import com.example.netpick_movil.viewmodel.UsuarioFormEvent
import com.example.netpick_movil.viewmodel.UsuarioViewModel

@Composable
fun UserFormScreen(
    modifier: Modifier = Modifier,
    viewModel: UsuarioViewModel = usuarioViewModel(),
    onRegisterSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val errores = uiState.errores

    LaunchedEffect(uiState.errores.registro) {
        if (uiState.errores.registro?.contains("éxito") == true) {
            kotlinx.coroutines.delay(2000)
            onRegisterSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextField(
            value = uiState.nombre,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.NombreChanged(it)) },
            label = { Text("Nombre") },
            isError = errores.nombre != null,
            modifier = Modifier.fillMaxWidth()
        )
        errores.nombre?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.correo,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.CorreoChanged(it)) },
            label = { Text("Correo") },
            isError = errores.correo != null,
            modifier = Modifier.fillMaxWidth()
        )
        errores.correo?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.clave,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.ClaveChanged(it)) },
            label = { Text("Clave") },
            isError = errores.clave != null,
            modifier = Modifier.fillMaxWidth()
        )
        errores.clave?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.direccion,
            onValueChange = { viewModel.onEvent(UsuarioFormEvent.DireccionChanged(it)) },
            label = { Text("Dirección") },
            isError = errores.direccion != null,
            modifier = Modifier.fillMaxWidth()
        )
        errores.direccion?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.aceptaTerminos,
                onCheckedChange = { viewModel.onEvent(UsuarioFormEvent.AceptaTerminosChanged(it)) }
            )
            Text("Acepto los términos y condiciones")
        }
        errores.aceptaTerminos?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        errores.registro?.let { mensaje ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensaje,
                color = if (mensaje.contains("éxito")) Color.Green else MaterialTheme.colorScheme.error,
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