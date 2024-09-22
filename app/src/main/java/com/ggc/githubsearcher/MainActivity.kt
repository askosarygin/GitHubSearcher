package com.ggc.githubsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ggc.domain.InteractorImpl
import com.ggc.domain.usecases.GetRepositoryContentUseCase
import com.ggc.domain.usecases.SearchInGitHubByTextUseCase
import com.ggc.githubsearcher.di.DI
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.screen_main.ScreenMain
import com.ggc.ui.screen_main.ScreenMainViewModel
import com.ggc.ui.screen_repository_content.ScreenRepositoryContent
import com.ggc.ui.screen_repository_content.ScreenRepositoryContentViewModel
import com.ggc.ui.theme.GitHubSearcherTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            GitHubSearcherTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.ScreenMain().route
                ) {
                    composable(route = NavRoutes.ScreenMain().route) {
                        val viewModel = viewModel(
                            initializer = {
                                ScreenMainViewModel(
                                    DI.getInteractor()
                                )
                            }
                        )

                        ScreenMain(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    composable(route = NavRoutes.ScreenRepositoryContent().route) {
                        val viewModel = viewModel(
                            initializer = {
                                ScreenRepositoryContentViewModel(
                                    DI.getInteractor()
                                )
                            }
                        )

                        ScreenRepositoryContent(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


