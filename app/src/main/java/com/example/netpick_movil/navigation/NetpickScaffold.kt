package com.example.netpick_movil.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.netpick_movil.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetpickScaffold(
    currentRoute: String?,
    onNavigationItemClick: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Netpick") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                windowInsets = WindowInsets.statusBars
            )
        },
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                NavigationBar(
                    windowInsets = WindowInsets.navigationBars
                ) {
                    val navItemColors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedTextColor = Color.Blue,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val LabelWithLine: @Composable (String, Boolean) -> Unit = { text, isSelected ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text)
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(2.dp)
                                        .width(24.dp)
                                        .background(Color.Blue)
                                )
                            }
                        }
                    }


                    NavigationBarItem(
                        icon = { },
                        label = { LabelWithLine("Inicio", currentRoute == Screen.Home.route) },
                        selected = currentRoute == Screen.Home.route,
                        onClick = { onNavigationItemClick(Screen.Home.route) },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { LabelWithLine("Categorías", currentRoute == Screen.Categories.route) },
                        selected = currentRoute == Screen.Categories.route,
                        onClick = { onNavigationItemClick(Screen.Categories.route) },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { LabelWithLine("Favoritos", currentRoute == Screen.Favorites.route) },
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = { onNavigationItemClick(Screen.Favorites.route) },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { LabelWithLine("Carrito", currentRoute == Screen.Cart.route) },
                        selected = currentRoute == Screen.Cart.route,
                        onClick = { onNavigationItemClick(Screen.Cart.route) },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        icon = { },
                        label = { LabelWithLine("Perfil", currentRoute == Screen.Profile.route) },
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { onNavigationItemClick(Screen.Profile.route) },
                        colors = navItemColors
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