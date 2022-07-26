package net.phadata.billing.model.api

import net.phadata.billing.constant.ResultCode


/**
 * 统一返回
 * @author linx
 * @since 2022-06-13 21:07
 *
 */
class ApiResult<T> {
    var code: String = ResultCode.FAILED.code
    var message: String? = null
    var payload: T? = null

    companion object {
        fun <T> success(payload: T): ApiResult<T> {
            val apiResult = ApiResult<T>()
            apiResult.code = ResultCode.SUCCESS.code
            apiResult.message = ResultCode.SUCCESS.msg
            apiResult.payload = payload
            return apiResult
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

        fun <T> result(code: String, message: String, payload: T?): ApiResult<T?> {
            return buildApiResult(code, message, payload)
        }

        fun <T> result(code: String, message: String?): ApiResult<T?> {
            return buildApiResult<T?>(code, message, null)
        }

        private fun <T> buildApiResult(code: String, message: String?, payload: T?): ApiResult<T?> {
            val apiResult = ApiResult<T?>()
            apiResult.code = code
            apiResult.message = message
            apiResult.payload = payload
            return apiResult
        }
    }

    override fun toString(): String {
        return "ApiResult(code='$code', message=$message, payload=$payload)"
    }

}