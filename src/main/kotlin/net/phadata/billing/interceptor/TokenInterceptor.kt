package net.phadata.billing.interceptor

import net.phadata.billing.exception.ServiceException
import net.phadata.billing.utils.CommonUtil
import net.phadata.billing.utils.JwtUtil
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * token校验拦截器
 * @author linx
 * @since 2022-06-14 00:34
 *
 */
class TokenInterceptor : HandlerInterceptor {
    private val log = LoggerFactory.getLogger(TokenInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 如果不是映射到方法直接通过
        if (handler !is HandlerMethod) {
            return true
        }

        //判断开启token验证
        log.info("校验token开始")
        val token = request.getHeader(CommonUtil.TOKEN)
        if (StringUtils.isBlank(token)) {
            throw ServiceException("token不能为空")
        }
        //验证过期时间
        if (JwtUtil.isExpiration(token)) {
            throw ServiceException("登陆已过期，请重新登陆")
        }
        log.info("校验token结束")
        return true
    }
}