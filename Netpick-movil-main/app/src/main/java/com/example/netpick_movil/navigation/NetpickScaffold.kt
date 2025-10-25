package com.example.netpick_movil.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.netpick_movil.R
import com.example.netpick_movil.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetpickScaffold(
    currentRoute: String?,
    onNavigationItemClick: (String) -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Netpick") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { },
                        label = { Text("Inicio") },
                        selected = currentRoute == Screen.Home.route,
                        onClick = { onNavigationItemClick(Screen.Home.route) }
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { Text("Categorías") },
                        selected = currentRoute == Screen.Categories.route,
                        onClick = { onNavigationItemClick(Screen.Categories.route) }
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { Text("Favoritos") },
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = { onNavigationItemClick(Screen.Favorites.route) }
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { Text("Carrito") },
                        selected = currentRoute == Screen.Cart.route,
                        onClick = { onNavigationItemClick(Screen.Cart.route) }
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { Text("Perfil") },
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { onNavigationItemClick(Screen.Profile.route) }
                    )
                }
            }
        },
        content = content
    )
}

private fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf(
        Screen.Home.route,
        Screen.Categories.route,
        Screen.Favorites.route,
        Screen.Cart.route,
        Screen.Profile.route
    )
}