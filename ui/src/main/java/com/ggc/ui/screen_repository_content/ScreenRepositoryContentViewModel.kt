package com.ggc.ui.screen_repository_content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.navigation.nav_events.NavSingleLifeEvent
import com.ggc.ui_api.Interactor
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenRepositoryContentViewModel(
    private val interactor: Interactor
) : ViewModel() {
    private val _modelState = MutableStateFlow(Model())
    val modelState = _modelState.asStateFlow()

    private val contentCache = MutableStateFlow(mutableListOf<Content>())

    fun init(owner: String, repo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.getRepositoryContent(owner, repo)

            when (result.resultCode) {
                GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> TODO()
                GetRepositoryContentResult.ResultCode.NO_INTERNET -> TODO()
                GetRepositoryContentResult.ResultCode.HTTP_ERROR -> TODO()

                GetRepositoryContentResult.ResultCode.OK -> {
                    contentCache.value.add(result.content)
                    updateContent(result.content)
                }
            }
        }
    }

    fun menuItemFolderClicked(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.getRepositoryContent(
                modelState.value.currentContent.owner,
                modelState.value.currentContent.repo,
                path
            )

            when (result.resultCode) {
                GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> TODO()
                GetRepositoryContentResult.ResultCode.NO_INTERNET -> TODO()
                GetRepositoryContentResult.ResultCode.HTTP_ERROR -> TODO()

                GetRepositoryContentResult.ResultCode.OK -> {
                    contentCache.value.add(result.content)
                    updateContent(result.content)
                }
            }
        }
    }

    fun buttonBackClicked() {
        contentCache.value.removeLastOrNull()
        if (contentCache.value.isNotEmpty()) {
            updateContent(contentCache.value.last())
        } else {
            updateNavEvent(NavSingleLifeEvent(NavRoutes.ScreenMain))
        }
    }

    data class Model(
        val currentContent: Content = Content("", "", listOf(), listOf()),
        val navEvent: NavSingleLifeEvent? = null
    )

    private fun updateNavEvent(navEvent: NavSingleLifeEvent) {
        _modelState.update { currentState ->
            currentState.copy(navEvent = navEvent)
        }
    }

    private fun updateContent(content: Content) {
        _modelState.update { currentState ->
            currentState.copy(currentContent = content)
        }
    }
}