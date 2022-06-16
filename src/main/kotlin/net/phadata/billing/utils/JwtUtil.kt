package net.phadata.billing.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Verification
import net.phadata.billing.exception.ServiceException
import net.phadata.billing.model.login.UserInfo
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.util.*


/**
 *
 * @author linx
 * @since 2022-06-13 23:50
 *
 */

/**
 * jwt工具类
 *
 * @author linx
 * @since 2022-05-06 11:24
 */

object JwtUtil {
    private val log = LoggerFactory.getLogger(BCryptPasswordEncoder::class.java)
    private const val DAY_MILLIS = 7 * 24 * 60 * 60 * 1000
    private const val ISSUER = "outlets"
    private const val SECRET = "i am linx"
    private const val INFO = "info"

    fun create(subject: String?, userInfo: UserInfo?): String {
        var token = ""
        try {
            token = JWT.create()
                .withExpiresAt(Date(System.currentTimeMillis() + DAY_MILLIS))
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withClaim(INFO, JSONObject.toJSONString(userInfo))
                .sign(Algorithm.HMAC256(SECRET))
        } catch (unsupportedEncodingException: UnsupportedEncodingException) {
            log.error("创建JWT-TOKEN失败:{}", unsupportedEncodingException.message)
            unsupportedEncodingException.printStackTrace()
        }
        return token
    }

    fun getInfoJSONStr(token: String?): String {
        val claims = getClaim(token)
        val claim = claims[INFO]
        if (claim == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        return claim.asString()
    }

    fun getInfoJSONStr(): String {
        val claims = getClaim(CommonUtil.getCurrentToken())
        val claim = claims[INFO]
        if (claim == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        return claim.asString()
    }

    fun <T> getInfo(token: String?, clazz: Class<T>?): T {
        val claims = getClaim(token)
        val claim = claims[INFO]
        if (claim == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        val jsonStr = claim.asString()
        if (jsonStr == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        return JSON.parseObject(jsonStr, clazz)
    }

    fun <T> getInfo(clazz: Class<T>?): T {
        val claims = getClaim(CommonUtil.getCurrentToken())
        val claim = claims[INFO]
        if (claim == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        val jsonStr = claim.asString()
        if (jsonStr == null) {
            log.error("解析token异常")
            throw ServiceException("解析token异常")
        }
        return JSON.parseObject(jsonStr, clazz)
    }

    fun getSubject(token: String?): String {
        val decodedJWT = getDecodedJWT(token)
        val subject = decodedJWT.subject
        if (subject == null) {
            log.error("解析token，获取subject异常")
            throw ServiceException("解析token，获取subject异常")
        }
        return subject
    }

    fun getSubject(): String {
        val decodedJWT = getDecodedJWT(CommonUtil.getCurrentToken())
        val subject = decodedJWT.subject
        if (subject == null) {
            log.error("解析token，获取subject异常")
            throw ServiceException("解析token，获取subject异常")
        }
        return subject
    }

    private fun getClaim(token: String?): Map<String, Claim> {
        var require: Verification? = null
        try {
            require = JWT.require(Algorithm.HMAC256(SECRET))
        } catch (unsupportedEncodingException: UnsupportedEncodingException) {
            log.error("解析JWT-TOKEN失败:{}", unsupportedEncodingException.message)
            unsupportedEncodingException.printStackTrace()
        }
        val verify = require!!.build().verify(token)
        return verify.claims
    }

    private fun getDecodedJWT(token: String?): DecodedJWT {
        var require: Verification? = null
        try {
            require = JWT.require(Algorithm.HMAC256(SECRET))
        } catch (unsupportedEncodingException: UnsupportedEncodingException) {
            log.error("解析JWT-TOKEN失败:{}", unsupportedEncodingException.message)
            unsupportedEncodingException.printStackTrace()
        }
        val verify: DecodedJWT
        verify = try {
            require!!.build().verify(token)
        } catch (e: AlgorithmMismatchException) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e))
            throw ServiceException("token加密的算法和解密用的算法不一致!")
        } catch (e: SignatureVerificationException) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e))
            throw ServiceException("token签名无效!")
        } catch (e: TokenExpiredException) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e))
            throw TokenExpiredException("token令牌已过期!")
        } catch (e: InvalidClaimException) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e))
            throw ServiceException("token中claim被修改，所以token认证失败")
        } catch (e: Exception) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e))
            throw ServiceException("登录凭证无效（过期），请重新登录")
        }
        return verify
    }

    fun isExpiration(token: String?): Boolean {
        val decodedJWT = getDecodedJWT(token)
        return decodedJWT.expiresAt.before(Date())
    }

/*    @JvmStatic
    fun main(args: Array<String>) {
        val apply = UserInfo().apply {
            userId = 1
            account = "linx"
            nickName = "qzrs"
            roleId = 1
        }
        val userInfo = create("linx", apply)
        println(userInfo)
        val info = getInfo(userInfo, UserInfo::class.java)
        println(info)
    }*/
}