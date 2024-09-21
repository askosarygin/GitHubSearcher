package com.ggc.ui.navigation

sealed class NavRoutes(
    val route: String
) {
    object ScreenMain : NavRoutes("ScreenMain")
    object ScreenRepositoryContent : NavRoutes("ScreenRepositoryContent")
}