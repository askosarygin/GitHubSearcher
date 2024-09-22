package com.ggc.data_api.github.responses

data class ResponseResult<RESPONSE>(
    val resultCode: ResultCode,
    val resultMessage: String,
    val response: RESPONSE
) {
    enum class ResultCode {
        INTERNAL_ERROR,
        HTTP_ERROR,
        OK
    }
}
