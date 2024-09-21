package com.ggc.domain

import com.ggc.domain.usecases.GetRepositoryContentUseCase
import com.ggc.domain.usecases.SearchInGitHubByTextUseCase
import com.ggc.ui_api.Interactor
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult
import com.ggc.ui_api.usecases_results.SearchInGitHubByTextResult

class InteractorImpl(
    private val searchInGitHubByTextUseCase: SearchInGitHubByTextUseCase,
    private val getRepositoryContentUseCase: GetRepositoryContentUseCase
) : Interactor {
    override suspend fun searchInGitHubByText(text: String): SearchInGitHubByTextResult =
        searchInGitHubByTextUseCase.execute(text)

    override suspend fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String
    ): GetRepositoryContentResult = getRepositoryContentUseCase.execute(owner, repo, path)

    override suspend fun getRepositoryContent(
        owner: String,
        repo: String
    ): GetRepositoryContentResult = getRepositoryContent(owner, repo, "")
}