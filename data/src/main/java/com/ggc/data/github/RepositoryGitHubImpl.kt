package com.ggc.data.github

import com.ggc.data.github.api.GitHubApi
import com.ggc.data_api.github.RepositoryGitHub
import com.ggc.data_api.github.responses.Content
import com.ggc.data_api.github.responses.ResponseResult
import com.ggc.data_api.github.responses.ResponseSearchRepositories
import com.ggc.data_api.github.responses.ResponseSearchUsers
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

class RepositoryGitHubImpl(
    private val api: GitHubApi
) : RepositoryGitHub {
    override suspend fun searchUsersByName(userName: String): ResponseResult<ResponseSearchUsers> {
        try {
            val responseRaw = api.searchUsers(userName).execute()

            return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
                ResponseResult(
                    ResponseResult.ResultCode.OK,
                    "OK",
                    responseRaw.body()!!
                ) else {
                ResponseResult(
                    ResponseResult.ResultCode.HTTP_ERROR,
                    responseRaw.message(),
                    ResponseSearchUsers()
                )
            }
        } catch (e: Exception) {
            return ResponseResult(
                ResponseResult.ResultCode.INTERNAL_ERROR,
                "Internal error",
                ResponseSearchUsers()
            )
        }
    }

    override suspend fun searchRepositoriesByName(
        repositoryName: String
    ): ResponseResult<ResponseSearchRepositories> {
        try {
            val responseRaw = api.searchRepositories(repositoryName).execute()
            
            return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
                ResponseResult(
                    ResponseResult.ResultCode.OK,
                    "OK",
                    responseRaw.body()!!
                ) else {
                ResponseResult(
                    ResponseResult.ResultCode.HTTP_ERROR,
                    responseRaw.message(),
                    ResponseSearchRepositories()
                )
            }

        } catch (e: Exception) {
            return ResponseResult(
                ResponseResult.ResultCode.INTERNAL_ERROR,
                "Internal error",
                ResponseSearchRepositories()
            )
        }
    }

    override suspend fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String
    ): ResponseResult<ArrayList<Content>> {
        try {
            val responseRaw = api.getRepositoryContent(owner, repo, path).execute()

            return if (responseRaw.code() in 200..299 && responseRaw.body() != null)
                ResponseResult(
                    ResponseResult.ResultCode.OK,
                    "OK",
                    responseRaw.body()!!
                ) else {
                ResponseResult(
                    ResponseResult.ResultCode.HTTP_ERROR,
                    responseRaw.message(),
                    arrayListOf()
                )
            }
        }catch (e: Exception) {
            return ResponseResult(
                ResponseResult.ResultCode.INTERNAL_ERROR,
                "Internal error",
                arrayListOf()
            )
        }
    }
}