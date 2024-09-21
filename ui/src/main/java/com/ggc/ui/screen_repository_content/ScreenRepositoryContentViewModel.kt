package com.ggc.ui.screen_repository_content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggc.ui.navigation.nav_params.RepositoryInfo
import com.ggc.ui_api.Interactor
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenRepositoryContentViewModel(
    private val interactor: Interactor,
    private val navParams: RepositoryInfo?
) : ViewModel() {
    private val _modelState = MutableStateFlow(Model())
    val modelState = _modelState.asStateFlow()

    init {
        navParams?.let { repositoryInfo ->
            viewModelScope.launch(Dispatchers.IO) {
                val result =
                    interactor.getRepositoryContent(repositoryInfo.owner, repositoryInfo.repo)

                when (result.resultCode) {
                    GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> TODO()
                    GetRepositoryContentResult.ResultCode.NO_INTERNET -> TODO()
                    GetRepositoryContentResult.ResultCode.HTTP_ERROR -> TODO()
                    GetRepositoryContentResult.ResultCode.OK -> {
                        updateContent(result.content)
                    }
                }
            }
        }
    }

    fun menuItemFolderClicked(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.getRepositoryContent(
                modelState.value.content.owner,
                modelState.value.content.repo,
                path
            )

            when (result.resultCode) {
                GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> TODO()
                GetRepositoryContentResult.ResultCode.NO_INTERNET -> TODO()
                GetRepositoryContentResult.ResultCode.HTTP_ERROR -> TODO()
                GetRepositoryContentResult.ResultCode.OK -> {
                    updateContent(result.content)
                }
            }
        }
    }

    fun menuItemFileClicked(htmlUrl: String) {
        println("Кликнут файл со ссылкой $htmlUrl, открывается браузер")
    }

    data class Model(
        val content: Content = Content("", "", listOf(), listOf()),
    )

    private fun updateContent(content: Content) {
        _modelState.update { currentState ->
            currentState.copy(content = content)
        }
    }
}