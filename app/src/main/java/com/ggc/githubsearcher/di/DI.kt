package com.ggc.githubsearcher.di

import com.ggc.data.github.RepositoryGitHubImpl
import com.ggc.data.github.api.GitHubApi
import com.ggc.domain.InteractorImpl
import com.ggc.domain.usecases.GetRepositoryContentUseCase
import com.ggc.domain.usecases.SearchInGitHubByTextUseCase
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class DI {
    companion object {
        private val githubApiBaseUrl = "https://api.github.com/"

        private var gitHubApi: GitHubApi? = null
        private var repositoryGitHub: RepositoryGitHubImpl? = null
        private var searchInGitHubByTextUseCase: SearchInGitHubByTextUseCase? = null
        private var getRepositoryContentUseCase: GetRepositoryContentUseCase? = null
        private var interactor: InteractorImpl? = null

        private fun getGitHubApi(): GitHubApi {
            if (gitHubApi == null) {
                gitHubApi = Retrofit.Builder()
                    .baseUrl(githubApiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create<GitHubApi>()
            }

            return gitHubApi!!
        }

        private fun getRepositoryGitHub(): RepositoryGitHubImpl {
            if (repositoryGitHub == null) {
                repositoryGitHub = RepositoryGitHubImpl(getGitHubApi())
            }
            return repositoryGitHub!!
        }

        private fun getSearchInGitHubByTextUseCase(): SearchInGitHubByTextUseCase {
            if (searchInGitHubByTextUseCase == null) {
                searchInGitHubByTextUseCase = SearchInGitHubByTextUseCase(getRepositoryGitHub())
            }
            return searchInGitHubByTextUseCase!!
        }

        private fun getGetRepositoryContentUseCase(): GetRepositoryContentUseCase {
            if (getRepositoryContentUseCase == null) {
                getRepositoryContentUseCase = GetRepositoryContentUseCase(getRepositoryGitHub())
            }
            return getRepositoryContentUseCase!!
        }

        fun getInteractor(): InteractorImpl {
            if (interactor == null) {
                interactor = InteractorImpl(
                    getSearchInGitHubByTextUseCase(),
                    getGetRepositoryContentUseCase()
                )
            }
            return interactor!!
        }
    }
}