package com.example.netpick_movil.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.netpick_movil.viewmodel.UsuarioViewModel
import com.example.netpick_movil.viewmodel.UsuarioViewModelFactory
import com.example.netpick_movil.data.remote.dao.UsuarioDao

@Composable
fun ProfileScreen(
    navController: NavController,
    dao: UsuarioDao,
    correoUsuario: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(
            dao = dao,
            correoUsuario = correoUsuario
        )
    )

    val usuario = usuarioViewModel.usuario.collectAsState()

    LaunchedEffect(correoUsuario) {
        usuarioViewModel.cargarUsuarioPorCorreo(correoUsuario)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (usuario.value == null) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(20.dp))

                ProfileItem("Nombre", usuario.value!!.nombre)
                ProfileItem("Correo", usuario.value!!.correo)
                ProfileItem("Teléfono", usuario.value!!.telefono)
                ProfileItem("Dirección", usuario.value!!.direccion)

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
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
