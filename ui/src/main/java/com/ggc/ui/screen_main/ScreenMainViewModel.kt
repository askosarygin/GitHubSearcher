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
            if (modelState.searchResults.isNotEmpty()) updateSearchResults(listOf())
            updateTextFieldSearchEnabled(false)
            updateButtonSearchEnabled(false)
            updateShowProgressBar(true)
            val result = interactor.searchInGitHubByText(modelState.textFieldSearch)

            when (result.resultCode) {
                SearchInGitHubByTextResult.ResultCode.INTERNAL_ERROR -> TODO()
                SearchInGitHubByTextResult.ResultCode.NO_INTERNET -> TODO()
                SearchInGitHubByTextResult.ResultCode.HTTP_ERROR -> TODO()
                SearchInGitHubByTextResult.ResultCode.OK -> {
                    updateSearchResults(result.searchResults)
                }
            }
            updateTextFieldSearchEnabled(true)
            updateButtonSearchEnabled(true)
            updateShowProgressBar(false)
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

    data class Model(
        val textFieldSearchEnabled: Boolean = true,
        val buttonSearchEnabled: Boolean = false,
        val showProgressBar: Boolean = false,
        val textFieldSearch: String = "",
        val searchResults: List<SearchResult> = listOf(),
        val navEventData: NavSingleLifeEventWithNavArgs<RepositoryInfo>? = null
    )

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