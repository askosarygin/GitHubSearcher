package com.ggc.githubsearcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ggc.domain.InteractorImpl
import com.ggc.domain.usecases.GetRepositoryContentUseCase
import com.ggc.domain.usecases.SearchInGitHubByTextUseCase
import com.ggc.githubsearcher.di.DI
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.navigation.nav_params.RepositoryInfo
import com.ggc.ui.screen_main.ScreenMain
import com.ggc.ui.screen_main.ScreenMainViewModel
import com.ggc.ui.screen_repository_content.ScreenRepositoryContent
import com.ggc.ui.screen_repository_content.ScreenRepositoryContentViewModel
import com.ggc.ui.theme.GitHubSearcherTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            GitHubSearcherTheme {
                NavHost(navController = navController, startDestination = NavRoutes.ScreenMain.route) {

                    composable(route = NavRoutes.ScreenMain.route) {
                        ScreenMain(
                            viewModel = ScreenMainViewModel(
                                InteractorImpl(
                                    SearchInGitHubByTextUseCase(DI.getRepositoryGitHub()),
                                    GetRepositoryContentUseCase(DI.getRepositoryGitHub())
                                )
                            ),
                            navController = navController
                        )
                    }

                    composable(route = NavRoutes.ScreenRepositoryContent.route) {
                        val repositoryInfo = navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<RepositoryInfo>("NAV_EVENT_KEY_OWNER_REPO")

                        ScreenRepositoryContent(
                            viewModel = ScreenRepositoryContentViewModel(
                                InteractorImpl(
                                    SearchInGitHubByTextUseCase(DI.getRepositoryGitHub()),
                                    GetRepositoryContentUseCase(DI.getRepositoryGitHub())
                                ),
                                repositoryInfo
                            ),
                            navController = navController
                        )
                    }
                }


            }
        }
    }
}

@Preview(widthDp = 490, heightDp = 1060)
//@Preview(widthDp = 1060, heightDp = 490)
@Composable
private fun Preview() {
    GitHubSearcherTheme {
//        ScreenMain(
//            ScreenMainViewModel(
//                InteractorImpl(
//                    SearchInGitHubByTextUseCase(DI.getRepositoryGitHub())
//                )
//            )
//        )
    }
}


