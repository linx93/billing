package net.phadata.billing.network.impl

import net.phadata.billing.exception.ServiceException
import net.phadata.billing.interceptor.TokenInterceptor
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.auth.CreateTokenResponse
import net.phadata.billing.model.auth.VerifyTokenResponse
import net.phadata.billing.network.AuthCenterServerApi
import net.phadata.billing.network.BaseServerApi
import net.phadata.billing.utils.AuthCenterUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component


/**
 * 鉴权中心相关请求实现
 * @author linx
 * @since 2022-07-25 15:01
 *
 */
@Component
class AuthCenterServerApiImpl : AuthCenterServerApi, BaseServerApi() {
    private val log = LoggerFactory.getLogger(TokenInterceptor::class.java)

    private val verify = "api/v1/auth/token/verify"
    private val create = "api/v1/auth/token/create"

    @Value("\${authCenter.appKey}")
    private val appKey: String = ""

    @Value("\${authCenter.appSecret}")
    private val appSecret: String = ""

    @Value("\${authCenter.server}")
    private val address: String = ""

    override fun createToken(): String {
        val randStr = AuthCenterUtil.randStr()
        val timestampStr = AuthCenterUtil.timestampStr()
        val sign = AuthCenterUtil.getSign(appKey, appSecret, randStr, timestampStr)
        val httpHeaders = HttpHeaders().apply {
            add("x-appKey", appKey)
            add("x-signature", sign)
            add("x-timestamp", timestampStr)
            add("x-rand", randStr)
            contentType = MediaType.APPLICATION_JSON
        }
        val httpEntity = HttpEntity(null, httpHeaders)
        val str = restTemplate.postForObject(
            buildUrl(address, create), httpEntity,
            String::class.java
        )
        str ?: throw ServiceException("请求授权中心创建token时返回为空")
        val buildApiResult = buildApiResult(str, CreateTokenResponse::class.java)
        if ("200000" != buildApiResult.code) {
            log.error("鉴权中心鉴权出错:${buildApiResult.message}")
            throw ServiceException("鉴权中心鉴权出错:${buildApiResult.message}")
        }
        val token = buildApiResult.payload?.token
        if (token == null) {
            log.error("鉴权中心鉴权出错:${buildApiResult.message}")
            throw ServiceException("鉴权中心鉴权出错:${buildApiResult.message}")
        }
        return token
    }

    override fun verifyToken(token: String): Boolean {
        val httpHeaders = HttpHeaders().apply {
            add(AuthCenterUtil.token, token)
        }
        val httpEntity = HttpEntity(null, httpHeaders)
        val str = restTemplate.postForObject(
            buildUrl(address, verify),
            httpEntity,
            String::class.java
        )
        if (str == null) {
            log.error("请求授权中心校验token时返回为空")
            throw ServiceException("请求授权中心校验token时返回为空")
        }
        val buildApiResult = buildApiResult(str, VerifyTokenResponse::class.java)
        if ("200000" != buildApiResult.code) {
            log.error("鉴权中心鉴权不通过:${buildApiResult.message}")
            throw ServiceException("鉴权中心鉴权出错:${buildApiResult.message}")
        }
        if (buildApiResult.payload?.verify == false) {
            log.error("鉴权中心鉴权不通过:${buildApiResult.payload?.err}")
            throw ServiceException("鉴权中心鉴权不通过:${buildApiResult.payload?.err}")
        }
        return true
    }


}