package com.ggc.ui.screen_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.navigation.nav_events.NavSingleLifeEventWithNavArgs
import com.ggc.ui.navigation.nav_params.RepositoryInfo
import com.ggc.ui_api.Interactor
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenMainViewModel(
    private val interactor: Interactor
) : ViewModel() {

    private val _modelState = MutableStateFlow(Model())
    val modelStateFlow = _modelState.asStateFlow()

    private val modelState: Model
        get() {
            return modelStateFlow.value
        }

    fun textFieldSearchChanged(newTextFieldValue: String) {
        updateButtonSearchEnabled(!(newTextFieldValue.length < 3))
        updateTextFieldSearch(newTextFieldValue)
    }

    fun buttonSearchClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            updateTextFieldSearchEnabled(false)
            updateButtonSearchEnabled(false)
            updateShowProgressBar(true)
            val result = interactor.searchInGitHubByText(modelState.textFieldSearch)

            when (result.resultCode) {
                SearchInGitHubByTextResult.ResultCode.INTERNAL_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateshowErrorBanner(true)
                }
                SearchInGitHubByTextResult.ResultCode.HTTP_ERROR -> {
                    updateErrorBannerMessage(result.resultMessage)
                    updateShowProgressBar(false)
                    updateshowErrorBanner(true)
                }
                SearchInGitHubByTextResult.ResultCode.OK -> {
                    updateTextFieldSearchEnabled(true)
                    updateButtonSearchEnabled(true)
                    updateShowProgressBar(false)
                    updateSearchResults(result.searchResults)
                }
            }
        }
    }

    fun repositoryClicked(owner: String, repo: String) {
        updateNavEventData(
            NavSingleLifeEventWithNavArgs(
                NavRoutes.ScreenRepositoryContent,
                RepositoryInfo(owner, repo)
            )
        )
    }

    fun buttonTryAgainClicked() {
        updateshowErrorBanner(false)
        updateShowProgressBar(true)
        buttonSearchClicked()
    }

    fun errorBannerCloseClicked() {
        updateTextFieldSearchEnabled(true)
        updateButtonSearchEnabled(true)
        updateshowErrorBanner(false)
    }

    data class Model(
        val textFieldSearchEnabled: Boolean = true,
        val buttonSearchEnabled: Boolean = false,
        val showProgressBar: Boolean = false,
        val showErrorBanner: Boolean = false,
        val errorBannerMessage: String = "",
        val textFieldSearch: String = "",
        val searchResults: List<SearchResult> = listOf(),
        val navEventData: NavSingleLifeEventWithNavArgs<RepositoryInfo>? = null
    )

    private fun updateshowErrorBanner(showErrorBanner: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(showErrorBanner = showErrorBanner)
        }
    }

    private fun updateErrorBannerMessage(errorBannerMessage: String) {
        _modelState.update { currentState ->
            currentState.copy(errorBannerMessage = errorBannerMessage)
        }
    }

    private fun updateTextFieldSearchEnabled(textFieldSearchEnabled: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(textFieldSearchEnabled = textFieldSearchEnabled)
        }
    }

    private fun updateButtonSearchEnabled(buttonSearchEnabled: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(buttonSearchEnabled = buttonSearchEnabled)
        }
    }

    private fun updateShowProgressBar(showProgressBar: Boolean) {
        _modelState.update { currentState ->
            currentState.copy(showProgressBar = showProgressBar)
        }
    }

    private fun updateTextFieldSearch(textFieldSearch: String) {
        _modelState.update { currentState ->
            currentState.copy(textFieldSearch = textFieldSearch)
        }
    }

    private fun updateSearchResults(searchResults: List<SearchResult>) {
        _modelState.update { currentState ->
            currentState.copy(searchResults = searchResults)
        }
    }

    private fun updateNavEventData(navEventData: NavSingleLifeEventWithNavArgs<RepositoryInfo>?) {
        _modelState.update { currentState ->
            currentState.copy(navEventData = navEventData)
        }
    }
}