package com.example.netpick_movil.ui.screen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object UserForm : Screen("user_form")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Categories : Screen("categories")
    object Favorites : Screen("favorites")
    object Cart : Screen("cart")
    object Confirmation : Screen("confirmation")
}