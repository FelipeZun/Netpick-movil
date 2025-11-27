package com.example.netpick_movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.netpick_movil.data.local.AppDatabase
import androidx.navigation.compose.rememberNavController
import com.example.netpick_movil.navigation.AppNavigation
import com.example.netpick_movil.ui.screen.HomeScreen
import com.example.netpick_movil.ui.theme.NetpickMovilTheme
import com.example.netpick_movil.viewmodel.AuthViewModel
import com.example.netpick_movil.viewmodel.AuthViewModelFactory
import com.example.netpick_movil.viewmodel.UsuarioViewModel
import com.example.netpick_movil.viewmodel.UsuarioViewModelFactory
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetpickMovilTheme {
                AppNavigation(modifier = Modifier.fillMaxSize())
                FirebaseApp.initializeApp(this)

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NetpickMovilTheme {
        AppNavigation()
        HomeScreen(navController = rememberNavController())
    }
}

