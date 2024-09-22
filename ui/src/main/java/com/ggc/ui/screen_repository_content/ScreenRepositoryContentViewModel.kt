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
    val modelStateFlow = _modelState.asStateFlow()

    private val modelState: Model
        get() {
            return modelStateFlow.value
        }

    private val contentCache = mutableListOf<Content>()
    private var initOwner = ""
    private var initRepo = ""
    private var lastClickedFolderPath = ""

    fun init(owner: String, repo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            initOwner = owner
            initRepo = repo
            updateShowProgressBar(true)
            val result = interactor.getRepositoryContent(owner, repo)

            when (result.resultCode) {
                GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateShowErrorBanner(true)
                }
                GetRepositoryContentResult.ResultCode.HTTP_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateShowErrorBanner(true)
                }

                GetRepositoryContentResult.ResultCode.OK -> {
                    contentCache.add(result.content)
                    updateShowProgressBar(false)
                    updateCurrentContent(result.content)
                }
            }
        }
    }

    fun menuItemFolderClicked(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            lastClickedFolderPath = path
            updateShowProgressBar(true)
            val result = interactor.getRepositoryContent(
                modelState.currentContent.owner,
                modelState.currentContent.repo,
                path
            )

            when (result.resultCode) {
                GetRepositoryContentResult.ResultCode.INTERNAL_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateShowErrorBanner(true)
                }
                GetRepositoryContentResult.ResultCode.HTTP_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateShowErrorBanner(true)
                }

                GetRepositoryContentResult.ResultCode.OK -> {
                    contentCache.add(result.content)
                    updateShowProgressBar(false)
                    updateCurrentContent(result.content)
                }
            }
        }
    }

    fun buttonBackClicked() {
        contentCache.removeLastOrNull()
        if (contentCache.isNotEmpty()) {
            updateCurrentContent(contentCache.last())
        } else {
            updateNavEvent(NavSingleLifeEvent(NavRoutes.ScreenMain))
        }
    }

    fun buttonTryAgainClicked() {
        updateShowErrorBanner(false)
        updateShowProgressBar(true)
        if (modelState.currentContent.files.isEmpty() && modelState.currentContent.folders.isEmpty()) {
            init(initOwner, initRepo)
        } else {
            menuItemFolderClicked(lastClickedFolderPath)
        }
    }

    fun errorBannerCloseClicked() {
        if (modelState.currentContent.files.isEmpty() && modelState.currentContent.folders.isEmpty()) {
            buttonBackClicked()
        } else {
            updateShowErrorBanner(false)
            updateCurrentContent(contentCache.last())
        }
    }

    data class Model(
        val showErrorBanner: Boolean = false,
        val errorBannerMessage: String = "",
        val showProgressBar: Boolean = false,
        val currentContent: Content = Content("", "", listOf(), listOf()),
        val navEvent: NavSingleLifeEvent? = null
    )

    private fun updateShowErrorBanner(showErrorBanner: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(showErrorBanner = showErrorBanner)
        }
    }

    private fun updateErrorBannerMessage(errorBannerMessage: String) {
        _modelState.update { currentState ->
            currentState.copy(errorBannerMessage = errorBannerMessage)
        }
    }

    private fun updateShowProgressBar(showProgressBar: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(showProgressBar = showProgressBar)
        }
    }

    private fun updateNavEvent(navEvent: NavSingleLifeEvent) {
        _modelState.update { currentState ->
            currentState.copy(navEvent = navEvent)
        }
    }

    private fun updateCurrentContent(content: Content) {
        _modelState.update { currentState ->
            currentState.copy(currentContent = content)
        }
    }
}