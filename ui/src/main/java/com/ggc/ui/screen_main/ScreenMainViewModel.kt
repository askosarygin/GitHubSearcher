package com.ggc.ui.screen_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggc.ui.navigation.NavRoutes
import com.ggc.ui.navigation.nav_events.NavEventData
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
    val modelState = _modelState.asStateFlow()

    fun textFieldSearchChanged(newTextFieldValue: String) {
        _modelState.update { currentState ->
            currentState.copy(textFieldSearch = newTextFieldValue)
        }
    }

    fun buttonSearchPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.searchInGitHubByText(modelState.value.textFieldSearch)

            when (result.resultCode) {
                SearchInGitHubByTextResult.ResultCode.INTERNAL_ERROR -> TODO()
                SearchInGitHubByTextResult.ResultCode.NO_INTERNET -> TODO()
                SearchInGitHubByTextResult.ResultCode.HTTP_ERROR -> TODO()
                SearchInGitHubByTextResult.ResultCode.OK -> {
                    _modelState.update { currentState ->
                        currentState.copy(searchResults = result.searchResults)
                    }
                }
            }
        }
    }

    fun repositoryClicked(owner: String, repo: String) {
        _modelState.update { currentState ->
            currentState.copy(
                navEventData = NavEventData(
                    NavRoutes.ScreenRepositoryContent,
                    RepositoryInfo(owner, repo)
                )
            )
        }
    }

    data class Model(
        val textFieldSearch: String = "",
        val searchResults: List<SearchResult> = listOf(),
        val navEventData: NavEventData<RepositoryInfo>? = null
    )
}