package com.ggc.ui_api.usecases_results

data class GetRepositoryContentResult(
    val resultCode: ResultCode,
    val content: Content = Content("", "", listOf(), listOf())
) {
    data class Content(
        val owner: String,
        val repo: String,
        val folders: List<Folder>,
        val files: List<File>
    ) {
        data class Folder(
            val name: String,
            val path: String
        )

        data class File(
            val name: String,
            val htmlUrl: String
        )
    }

    enum class ResultCode {
        INTERNAL_ERROR,
        NO_INTERNET,
        HTTP_ERROR,
        OK
    }
}
