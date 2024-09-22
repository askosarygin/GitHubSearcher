package com.ggc.ui.navigation

sealed class NavRoutes {
    class ScreenMain() : NavRoutes() {
        val route: String = "ScreenMain"
    }

    class ScreenRepositoryContent(private val args: Args? = null) : NavRoutes() {
        val route: String = "ScreenRepositoryContent/{owner}/{repo}"
        val routeWithAttachedArgs: String
            get() {
                return "ScreenRepositoryContent/${args?.owner}/${args?.repo}"
            }

        data class Args(
            val owner: String,
            val repo: String
        )
    }
}