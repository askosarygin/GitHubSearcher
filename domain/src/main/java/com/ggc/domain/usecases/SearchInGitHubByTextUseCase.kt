package com.ggc.domain.usecases

import com.ggc.data_api.github.RepositoryGitHub
import com.ggc.data_api.github.responses.ResponseResult
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.ResultCode.HTTP_ERROR
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.ResultCode.INTERNAL_ERROR
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.ResultCode.NO_INTERNET
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.ResultCode.OK
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult.SearchResult

class SearchInGitHubByTextUseCase(
    private val repositoryGitHub: RepositoryGitHub
) {
    suspend fun execute(text: String): SearchInGitHubByTextResult {
        val searchResults = mutableListOf<SearchResult>()

        val searchRepositoriesResult = repositoryGitHub.searchRepositoriesByName(text)
        when (searchRepositoriesResult.resultCode) {
            ResponseResult.ResultCode.INTERNAL_ERROR ->
                return SearchInGitHubByTextResult(INTERNAL_ERROR)

            ResponseResult.ResultCode.NO_INTERNET ->
                return SearchInGitHubByTextResult(NO_INTERNET)

            ResponseResult.ResultCode.HTTP_ERROR ->
                return SearchInGitHubByTextResult(HTTP_ERROR)

            ResponseResult.ResultCode.OK -> {
                searchRepositoriesResult.response.items.forEach { repository ->
                    searchResults.add(
                        SearchResult(
                            name = repository.name.toString(),
                            repository = SearchResult.Repository(
                                repository.forks ?: 0,
                                repository.description.toString(),
                                repository.owner?.login.toString(),
                                repository.name.toString()
                            )
                        )
                    )
                }
            }
        }

        val searchUsersResult = repositoryGitHub.searchUsersByName(text)
        when (searchUsersResult.resultCode) {
            ResponseResult.ResultCode.INTERNAL_ERROR ->
                return SearchInGitHubByTextResult(INTERNAL_ERROR)

            ResponseResult.ResultCode.NO_INTERNET ->
                return SearchInGitHubByTextResult(NO_INTERNET)

            ResponseResult.ResultCode.HTTP_ERROR ->
                return SearchInGitHubByTextResult(HTTP_ERROR)

            ResponseResult.ResultCode.OK -> {
                searchUsersResult.response.items.forEach { user ->
                    searchResults.add(
                        SearchResult(
                            name = user.login.toString(),
                            user = SearchResult.User(
                                user.avatarUrl.toString(),
                                user.score.toString(),
                                user.htmlUrl.toString()
                            )
                        )
                    )
                }
            }
        }

        return SearchInGitHubByTextResult(
            OK,
            searchResults.sortedBy { it.name }
        )
    }
}