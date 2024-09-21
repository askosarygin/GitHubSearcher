package com.ggc.githubsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggc.domain.InteractorImpl
import com.ggc.domain.usecases.GetRepositoryContentUseCase
import com.ggc.domain.usecases.SearchInGitHubByTextUseCase
import com.ggc.githubsearcher.di.DI
import com.ggc.ui.R
import com.ggc.ui.R.drawable.icon_file
import com.ggc.ui.R.drawable.icon_folder
import com.ggc.ui.screen_main.ScreenMain
import com.ggc.ui.screen_main.ScreenMainViewModel
import com.ggc.ui.theme.GitHubSearcherTheme
import com.ggc.ui.theme.RepositoryContentDivider
import com.ggc.ui.theme.RepositoryContentFile
import com.ggc.ui.theme.RepositoryContentFolder
import com.ggc.ui.theme.RepositoryContentText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubSearcherTheme {
                ScreenMain(
                    viewModel = ScreenMainViewModel(
                        InteractorImpl(
                            SearchInGitHubByTextUseCase(DI.getRepositoryGitHub()),
                            GetRepositoryContentUseCase(DI.getRepositoryGitHub())
                        )
                    )
                )
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


