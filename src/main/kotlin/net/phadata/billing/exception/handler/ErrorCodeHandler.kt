package net.phadata.billing.exception.handler

import com.auth0.jwt.exceptions.TokenExpiredException
import net.phadata.billing.constant.ResultCode
import net.phadata.billing.exception.BasicException
import net.phadata.billing.exception.ClientException
import net.phadata.billing.exception.ServiceException
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.utils.BCryptPasswordEncoder
import org.slf4j.LoggerFactory
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.net.SocketException


/**
 * 异常统一处理
 * @author linx
 * @since 2022-06-14 16:19
 *
 */

@RestControllerAdvice
class ErrorCodeHandler {
    private val log = LoggerFactory.getLogger(BCryptPasswordEncoder::class.java)

    @ExceptionHandler(value = [BasicException::class])
    fun commonExceptionHandler(ex: BasicException): ApiResult<Any?> {
        return when (ex) {
            is ServiceException -> run {
                log.error("ServiceException:{}", ex.message)
                ApiResult.result<Any>(ex.code, ex.message)
            }
            is ClientException -> run {
                log.error("ServiceException:{}", ex.message)
                ApiResult.result(ex.code, ex.message)
            }
            else -> run {
                log.error("ServiceException:{}", ex.message)
                ApiResult.result(ex.code, ex.message)
            }
        }
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun exceptionHandler(e: HttpRequestMethodNotSupportedException): ApiResult<Any?> {
        log.error("请求类型错误:{}", e.message)
        return ApiResult.result(ResultCode.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(e: BindException): ApiResult<Any?> {
        log.error("参数验证失败:{}", e.message)
        val error = e.fieldError
        val message = String.format("%s:%s", error?.field ?: "", if (error == null) "" else error.defaultMessage)
        log.error("参数验证失败:{}", message)
        return ApiResult.result(ResultCode.BAD_REQUEST.code, message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun exceptionHandler(e: MethodArgumentNotValidException): ApiResult<Any?> {
        val fieldErrors = e.bindingResult.fieldErrors
        val map: MutableMap<String, Any?> = HashMap(16)
        for (fieldError in fieldErrors) {
            map[fieldError.field] = fieldError.defaultMessage
        }
        log.error("参数验证失败:{}", map)
        val error = e.bindingResult.fieldError
        val message = String.format("%s:%s", error?.field ?: "", if (error == null) "" else error.defaultMessage)
        //todo Bad Request
        log.error("参数验证失败:{}", message)
        return ApiResult.result(ResultCode.BAD_REQUEST.code, message)
    }

    @ExceptionHandler(value = [SocketException::class])
    fun socketExceptionHandler(exception: SocketException): ApiResult<Any?> {
        log.error("网络请求错误: {}", exception.message)
        log.error("网络请求错误:", exception)
        return ApiResult.result(ResultCode.FAILED.code, exception.message)
    }

    @ExceptionHandler(value = [MaxUploadSizeExceededException::class])
    fun exceptionHandler(exception: MaxUploadSizeExceededException): ApiResult<Any?> {
        log.error("上传文件过大: {}", exception.message)
        return ApiResult.result(ResultCode.FAILED.code, "上传文件大于32MB")
    }

    @ExceptionHandler(value = [TokenExpiredException::class])
    fun exceptionHandler(exception: TokenExpiredException): ApiResult<Any?> {
        log.error("token已过期: {}", exception.message)
        log.error("异常信息:", exception)
        return ApiResult.result(ResultCode.FAILED.code, exception.message)
    }

    @ExceptionHandler(value = [Exception::class])
    fun exceptionHandler(exception: Exception): ApiResult<Any?> {
        log.error("服务器错误: {}", exception.message)
        log.error("服务器错误:", exception)
        return ApiResult.result(ResultCode.FAILED.code, exception.message)
    }
}