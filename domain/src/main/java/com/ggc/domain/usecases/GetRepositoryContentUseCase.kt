package com.ggc.domain.usecases

import com.ggc.data_api.github.RepositoryGitHub
import com.ggc.data_api.github.responses.ResponseResult
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.Content
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.Content.File
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.Content.Folder
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.ResultCode.HTTP_ERROR
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.ResultCode.INTERNAL_ERROR
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.ResultCode.NO_INTERNET
import com.ggc.ui_api.usecases_results.GetRepositoryContentResult.ResultCode.OK

class GetRepositoryContentUseCase(
    private val repositoryGitHub: RepositoryGitHub
) {
    suspend fun execute(
        owner: String,
        repo: String,
        path: String
    ): GetRepositoryContentResult {
        val getRepositoryContentResult = repositoryGitHub.getRepositoryContent(owner, repo, path)

        when (getRepositoryContentResult.resultCode) {
            ResponseResult.ResultCode.INTERNAL_ERROR ->
                return GetRepositoryContentResult(INTERNAL_ERROR)

            ResponseResult.ResultCode.NO_INTERNET ->
                return GetRepositoryContentResult(NO_INTERNET)

            ResponseResult.ResultCode.HTTP_ERROR ->
                return GetRepositoryContentResult(HTTP_ERROR)

            ResponseResult.ResultCode.OK -> {
                val folders = mutableListOf<Folder>()
                val files = mutableListOf<File>()

                getRepositoryContentResult.response.forEach { content ->
                    if (content.type == "dir") {
                        folders.add(
                            Folder(
                                content.name.toString(),
                                content.path.toString()
                            )
                        )
                    }
                    if (content.type == "file") {
                        files.add(
                            File(
                                content.name.toString(),
                                content.htmlUrl.toString()
                            )
                        )
                    }
                }

                return GetRepositoryContentResult(
                    OK,
                    Content(
                        owner,
                        repo,
                        folders,
                        files
                    )
                )
            }
        }
    }
}