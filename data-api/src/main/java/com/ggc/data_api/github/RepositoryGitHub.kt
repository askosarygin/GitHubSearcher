package com.ggc.data_api.github

import com.ggc.data_api.github.responses.Content
import com.ggc.data_api.github.responses.ResponseResult
import com.ggc.data_api.github.responses.ResponseSearchRepositories
import com.ggc.data_api.github.responses.ResponseSearchUsers

interface RepositoryGitHub {
    suspend fun searchUsersByName(userName: String): ResponseResult<ResponseSearchUsers>

    suspend fun searchRepositoriesByName(
        repositoryName: String
    ): ResponseResult<ResponseSearchRepositories>

    suspend fun getRepositoryContent(
        owner: String,
        repo: String,
        path: String
    ): ResponseResult<ArrayList<Content>>
}