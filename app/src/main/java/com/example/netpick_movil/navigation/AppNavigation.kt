package com.example.netpick_movil.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.netpick_movil.data.local.AppDatabase
import com.example.netpick_movil.data.remote.api.RetrofitClient
import com.example.netpick_movil.data.repository.CategoryRepository
import com.example.netpick_movil.data.repository.ProductoRepository
import com.example.netpick_movil.ui.screen.*
import com.example.netpick_movil.viewmodel.*

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val apiService = RetrofitClient.apiService
    val cartViewModel: CartViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory.Factory)
    val authState by authViewModel.uiState.collectAsState()

    val productDetailFactory = ProductDetailViewModelFactory(apiService = apiService)

    val startDestination = remember(authState.usuarioLogueado) {
        if (authState.usuarioLogueado != null) Screen.Home.route else Screen.Login.route
    }

    NetpickScaffold(
        currentRoute = currentRoute,
        onNavigationItemClick = { route ->
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Screen.Login.route) {
                AuthScreen(navController = navController, authViewModel = authViewModel)
            }

            composable(Screen.UserForm.route) {
                UserFormScreen(onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.UserForm.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Profile.route) {
                val dao = AppDatabase.getInstance(context).usuarioDao()
                val correoUsuario = authState.usuarioLogueado?.correo ?: ""
                ProfileScreen(
                    navController = navController,
                    dao = dao,
                    correoUsuario = correoUsuario,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Categories.route) {
                CategoriesScreen(navController = navController)
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(navController = navController, viewModel = favoritesViewModel)
            }

            composable(Screen.Cart.route) {
                CartScreen(navController = navController, viewModel = cartViewModel)
            }

            composable(Screen.Confirmation.route) {
                ConfirmationScreen(navController = navController)
            }

            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                val productDetailViewModel: ProductDetailViewModel =
                    viewModel(factory = productDetailFactory)

                LaunchedEffect(productId) {
                    productDetailViewModel.loadProduct(productId)
                }

                ProductDetailScreen(
                    viewModel = productDetailViewModel,
                    navController = navController,
                    productId = productId,
                    cartViewModel = cartViewModel,
                    favoritesViewModel = favoritesViewModel
                )
            }

            composable(
                route = "category/{categoryId}?name={categoryName}",
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.IntType },
                    navArgument("categoryName") { type = NavType.StringType; defaultValue = "Categoría" }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Categoría"

                CategoryDetailScreen(
                    navController = navController,
                    categoryId = categoryId,
                    categoryName = categoryName
                )
            }
        }
    }
}