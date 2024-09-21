package com.ggc.ui_api.usecases_results

data class SearchInGitHubByTextResult(
    val resultCode: ResultCode,
    val searchResults: List<SearchResult> = listOf()
) {
    data class SearchResult(
        val name: String,
        val user: User? = null,
        val repository: Repository? = null
    ) {
        data class User(
            val avatarUrl: String,
            val score: String,
            val htmlUrl: String
        )

        data class Repository(
            val forksCount: Int,
            val description: String,
            val owner: String,
            val repo: String
        )
    }

    enum class ResultCode {
        INTERNAL_ERROR,
        NO_INTERNET,
        HTTP_ERROR,
        OK
    }
}
