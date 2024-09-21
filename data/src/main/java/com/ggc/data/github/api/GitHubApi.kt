package com.ggc.data.github.api

import com.ggc.data_api.github.responses.Content
import com.ggc.data_api.github.responses.ResponseSearchRepositories
import com.ggc.data_api.github.responses.ResponseSearchUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/users")
    fun searchUsers(@Query("q") userName: String): Call<ResponseSearchUsers>

    @GET("search/repositories")
    fun searchRepositories(@Query("q") repositoryName: String): Call<ResponseSearchRepositories>

    @GET("repos/{owner}/{repo}/contents/{path}")
    fun getRepositoryContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Call<ArrayList<Content>>
}