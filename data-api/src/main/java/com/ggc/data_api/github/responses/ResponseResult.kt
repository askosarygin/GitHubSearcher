package com.ggc.data_api.github.responses

data class ResponseResult<RESPONSE>(
    val resultCode: ResultCode,
    val response: RESPONSE
) {
    enum class ResultCode {
        INTERNAL_ERROR,
        NO_INTERNET,
        HTTP_ERROR,
        OK
    }
}
