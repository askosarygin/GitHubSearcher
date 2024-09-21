package com.ggc.data.github

import com.ggc.data.github.api.GitHubApi
import com.ggc.data_api.github.RepositoryGitHub
import com.ggc.data_api.github.responses.Content
import com.ggc.data_api.github.responses.ResponseResult
import com.ggc.data_api.github.responses.ResponseSearchRepositories
import com.ggc.data_api.github.responses.ResponseSearchUsers

class RepositoryGitHubImpl(
    private val api: GitHubApi
) : RepositoryGitHub {
    override suspend fun searchUsersByName(userName: String): ResponseResult<ResponseSearchUsers> {
        val responseRaw = api.searchUsers(userName).execute()

        return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
            ResponseResult(
                ResponseResult.ResultCode.OK,
                responseRaw.body()!!
            ) else {
            ResponseResult(
                ResponseResult.ResultCode.HTTP_ERROR,
                ResponseSearchUsers()
            )
        }
    }

    override suspend fun searchRepositoriesByName(
        repositoryName: String
    ): ResponseResult<ResponseSearchRepositories> {
        val responseRaw = api.searchRepositories(repositoryName).execute()

        return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
            ResponseResult(
                ResponseResult.ResultCode.OK,
                responseRaw.body()!!
            ) else {
            ResponseResult(
                ResponseResult.ResultCode.HTTP_ERROR,
                ResponseSearchRepositories()
            )
        }
    }

    override suspend fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String
    ): ResponseResult<ArrayList<Content>> {
        val responseRaw = api.getRepositoryContent(owner, repo, path).execute()

        return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
            ResponseResult(
                ResponseResult.ResultCode.OK,
                responseRaw.body()!!
            ) else {
            ResponseResult(
                ResponseResult.ResultCode.HTTP_ERROR,
                arrayListOf()
            )
        }
    }
}