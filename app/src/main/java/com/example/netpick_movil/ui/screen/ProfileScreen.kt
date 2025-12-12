package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.netpick_movil.model.Usuario

@Composable
fun ProfileScreen(
    navController: NavController,
    usuario: Usuario?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (usuario == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No se encontró información del usuario.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = onLogout) {
                    Text("Volver al Login")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(20.dp))
                ProfileItem("Nombre", usuario.nombre ?: "Sin nombre")
                ProfileItem("Correo", usuario.correo)
                ProfileItem("Teléfono", usuario.telefono ?: "Sin teléfono")
                ProfileItem("Dirección", usuario.direccion ?: "Sin dirección")

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
        Divider(color = MaterialTheme.colorScheme.surfaceVariant) // Una línea para que se vea más ordenado
    }
}