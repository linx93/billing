package net.phadata.billing.network



/**
 * 鉴权中心相关请求
 * @author linx
 * @since 2022-07-25 15:00
 *
 */
interface AuthCenterServerApi {
    fun createToken(): String
    fun verifyToken(token: String): Boolean
}