package net.phadata.billing.utils

import net.phadata.billing.exception.ServiceException
import org.apache.commons.lang3.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


/**
 * 公共工具类
 * @author linx
 * @since 2022-06-13 23:59
 *
 */
object CommonUtil {
    const val TOKEN = "Authorization"
    const val BEARER = "Bearer "

    fun getCurrentToken(): String? {
        val requestAttributes =
            RequestContextHolder.getRequestAttributes() ?: throw ServiceException("获取requestAttributes失败")
        val request = (requestAttributes as ServletRequestAttributes).request
        val token = request.getHeader(TOKEN)
        if (StringUtils.isBlank(token)) {
            throw ServiceException("请求头中未携带Authorization导致获取token失败")
        }
        //todo 后续逻辑增加
        return token
    }
}