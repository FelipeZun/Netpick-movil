package com.example.netpick_movil.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.netpick_movil.ui.screen.*
import com.example.netpick_movil.viewmodel.AuthViewModel
import com.example.netpick_movil.viewmodel.AuthViewModelFactory
import com.example.netpick_movil.viewmodel.CartViewModel
import com.example.netpick_movil.viewmodel.FavoritesViewModel
import com.example.netpick_movil.viewmodel.ProductDetailViewModel
import com.example.netpick_movil.data.repository.ProductoRepository
import com.example.netpick_movil.viewmodel.ProductDetailViewModelFactory
import com.example.netpick_movil.viewmodel.UsuarioViewModel
import com.example.netpick_movil.viewmodel.UsuarioViewModelFactory

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).usuarioDao()

    val cartViewModel: CartViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory.Factory)
    val authState by authViewModel.uiState.collectAsState()

    val productoRepository = ProductoRepository()
    val productDetailFactory = ProductDetailViewModelFactory(productoRepository)

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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Screen.Login.route) {
                AuthScreen(navController = navController, authViewModel = authViewModel)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Profile.route) {

                val context = LocalContext.current
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


            composable(Screen.UserForm.route) {
                UserFormScreen(onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.UserForm.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Categories.route) {
                Text("Categorías - En desarrollo")
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
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productDetailViewModel: ProductDetailViewModel =
                    viewModel(factory = productDetailFactory)

                val productId = backStackEntry.arguments?.getInt("productId") ?: 0

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
        }
    }
}
