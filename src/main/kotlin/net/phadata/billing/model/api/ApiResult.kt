package net.phadata.billing.model.api

import net.phadata.billing.constant.ResultCode


/**
 * 统一返回
 * @author linx
 * @since 2022-06-13 21:07
 *
 */
class ApiResult<T> private constructor() {
    var code: Int = ResultCode.FAILED.code
        private set
    var message: String? = null
        private set
    var payload: T? = null
        private set

    companion object {
        fun <T> success(payload: T): ApiResult<T?> {
            return buildApiResult(ResultCode.SUCCESS.code, ResultCode.SUCCESS.msg, payload)
        }

        fun <T> fail(payload: T?): ApiResult<T?> {
            return buildApiResult(ResultCode.FAILED.code, ResultCode.FAILED.msg, payload)
        }

        fun <T> result(resultCode: ResultCode, payload: T): ApiResult<T?> {
            return buildApiResult(resultCode.code, resultCode.msg, payload)
        }

        fun <T> result(resultCode: ResultCode): ApiResult<T?> {
            return buildApiResult(resultCode.code, resultCode.msg, null)
        }

        fun <T> result(code: Int, message: String, payload: T?): ApiResult<T?> {
            return buildApiResult(code, message, payload)
        }

        fun <T> result(code: Int, message: String?): ApiResult<T?> {
            return buildApiResult<T?>(code, message, null)
        }

        private fun <T> buildApiResult(code: Int, message: String?, payload: T?): ApiResult<T?> {
            val apiResult = ApiResult<T?>()
            apiResult.code = code
            apiResult.message = message
            apiResult.payload = payload
            return apiResult
        }
    }
}