package com.ggc.ui_api

import com.ggc.ui_api.usecases_results.GetRepositoryContentResult
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult

interface Interactor {
    suspend fun searchInGitHubByText(text: String): SearchInGitHubByTextResult

    suspend fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String
    ): GetRepositoryContentResult

    suspend fun getRepositoryContent(
        owner: String,
        repo: String
    ): GetRepositoryContentResult
}