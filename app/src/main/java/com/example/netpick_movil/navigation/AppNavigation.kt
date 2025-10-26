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
import com.example.netpick_movil.ui.screen.CartScreen
import com.example.netpick_movil.ui.screen.HomeScreen
import com.example.netpick_movil.ui.screen.LoginScreen
import com.example.netpick_movil.ui.screen.ProductDetailScreen
import com.example.netpick_movil.ui.screen.ProfileScreen
import com.example.netpick_movil.ui.screen.Screen
import com.example.netpick_movil.ui.screen.UserFormScreen
import com.example.netpick_movil.viewmodel.CartViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val cartViewModel: CartViewModel = viewModel()

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
            modifier = modifier,
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.UserForm.route) {
                UserFormScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.UserForm.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Categories.route) {
                androidx.compose.material3.Text("Categorías")
            }
            composable(Screen.Favorites.route) {
                androidx.compose.material3.Text("Favoritos")
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    navController = navController,
                    viewModel = cartViewModel
                )
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    navController = navController,
                    productId = productId,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}