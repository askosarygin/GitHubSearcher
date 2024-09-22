package com.ggc.ui.screen_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.navigation.NavRoutes.ScreenRepositoryContent
import com.ggc.ui.navigation.NavRoutes.ScreenRepositoryContent.Args
import com.ggc.ui.navigation.nav_events.NavSingleLifeEvent
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
        _modelState.update { currentState ->
            currentState.copy(
                buttonSearchEnabled = !(newTextFieldValue.length < 3),
                textFieldSearch = newTextFieldValue
            )
        }
    }

    fun buttonSearchClicked() {
        viewModelScope.launch(Dispatchers.IO) {

            _modelState.update { currentState ->
                currentState.copy(
                    textFieldSearchEnabled = false,
                    buttonSearchEnabled = false,
                    showProgressBar = true
                )
            }

            val result = interactor.searchInGitHubByText(modelState.textFieldSearch)

            when (result.resultCode) {
                SearchInGitHubByTextResult.ResultCode.INTERNAL_ERROR -> {
                    _modelState.update { currentState ->
                        currentState.copy(
                            errorBannerMessage = result.resultMessage,
                            showProgressBar = false,
                            showErrorBanner = true
                        )
                    }
                }
                SearchInGitHubByTextResult.ResultCode.HTTP_ERROR -> {
                    _modelState.update { currentState ->
                        currentState.copy(
                            errorBannerMessage = result.resultMessage,
                            showProgressBar = false,
                            showErrorBanner = true
                        )
                    }
                }
                SearchInGitHubByTextResult.ResultCode.OK -> {
                    _modelState.update { currentState ->
                        currentState.copy(
                            textFieldSearchEnabled = true,
                            buttonSearchEnabled = true,
                            showProgressBar = false,
                            searchResults = result.searchResults
                        )
                    }
                }
            }
        }
    }

    fun repositoryClicked(owner: String, repo: String) {
        updateNavEventData(
            NavSingleLifeEvent(
                ScreenRepositoryContent(
                    Args(owner, repo)
                )
            )
        )
    }

    fun buttonTryAgainClicked() {
        _modelState.update { currentState ->
            currentState.copy(
                showErrorBanner = false,
                showProgressBar = true
            )
        }
        buttonSearchClicked()
    }

    fun errorBannerCloseClicked() {
        _modelState.update { currentState ->
            currentState.copy(
                textFieldSearchEnabled = true,
                buttonSearchEnabled = true,
                showErrorBanner = false
            )
        }
    }

    data class Model(
        val textFieldSearchEnabled: Boolean = true,
        val buttonSearchEnabled: Boolean = false,
        val showProgressBar: Boolean = false,
        val showErrorBanner: Boolean = false,
        val errorBannerMessage: String = "",
        val textFieldSearch: String = "",
        val searchResults: List<SearchResult> = listOf(),
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

    private fun updateNavEventData(navEvent: NavSingleLifeEvent?) {
        _modelState.update { currentState ->
            currentState.copy(navEvent = navEvent)
        }
    }
}