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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.netpick_movil.viewmodel.UsuarioFormEvent
import com.example.netpick_movil.viewmodel.UsuarioViewModel

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
