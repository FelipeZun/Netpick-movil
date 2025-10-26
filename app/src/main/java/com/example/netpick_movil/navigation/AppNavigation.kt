package com.example.netpick_movil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.netpick_movil.ui.screen.*
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val cartViewModel: CartViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()

    NetpickScaffold(
        currentRoute = currentRoute,
        onNavigationItemClick = { route ->
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
        ) {
            composable(Screen.Login.route) { LoginScreen(navController = navController) }
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.UserForm.route) {
                UserFormScreen(onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.UserForm.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Categories.route) {
                androidx.compose.material3.Text("Categorías - En desarrollo")
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    navController = navController,
                    viewModel = favoritesViewModel
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    navController = navController,
                    viewModel = cartViewModel
                )
            }
            composable(Screen.Confirmation.route) {
                ConfirmationScreen(navController = navController)
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    navController = navController,
                    productId = productId,
                    cartViewModel = cartViewModel,
                    favoritesViewModel = favoritesViewModel
                )
            }
        }
    }
}