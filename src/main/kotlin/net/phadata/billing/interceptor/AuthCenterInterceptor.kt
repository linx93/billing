package net.phadata.billing.interceptor

import net.phadata.billing.exception.ServiceException
import net.phadata.billing.utils.AuthCenterUtil
import net.phadata.billing.utils.CommonUtil
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 鉴权中心token校验
 * @author linx
 * @since 2022-07-25 14:33
 *
 */
@Component
class AuthCenterInterceptor : HandlerInterceptor {
    //@Autowired

    private val log = LoggerFactory.getLogger(AuthCenterInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 如果不是映射到方法直接通过
        if (handler !is HandlerMethod) {
            return super.preHandle(request, response, handler)
        }
        // 获取httpServletRequest中携带的授权中心颁发的token
        var authToken: String? = request.getHeader(AuthCenterUtil.token)
        if (StringUtils.isBlank(authToken)) {
            log.error("token不能为空")
            //throw ServiceException("token不能为空")
        }
        //去鉴权中校验token

        return super.preHandle(request, response, handler)
    }
}