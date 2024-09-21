package com.ggc.githubsearcher.di

import com.ggc.data.github.RepositoryGitHubImpl
import com.ggc.data.github.api.GitHubApi
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class DI {
    companion object {
        private val githubApiBaseUrl = "https://api.github.com/"

        private var gitHubApi: GitHubApi? = null
        private var repositoryGitHub: RepositoryGitHubImpl? = null

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

        fun getRepositoryGitHub(): RepositoryGitHubImpl {
            if (repositoryGitHub == null) {
                repositoryGitHub = RepositoryGitHubImpl(getGitHubApi())
            }
            return repositoryGitHub!!
        }
    }
}