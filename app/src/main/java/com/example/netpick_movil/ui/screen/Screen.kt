package com.example.netpick_movil.ui.screen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object UserForm : Screen("userform")
}
