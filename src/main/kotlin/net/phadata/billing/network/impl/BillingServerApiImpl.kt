package net.phadata.billing.network.impl

import net.phadata.billing.exception.ServiceException
import net.phadata.billing.model.billing.NotifyBillingRequest
import net.phadata.billing.network.AuthCenterServerApi
import net.phadata.billing.network.BaseServerApi
import net.phadata.billing.network.BillingServerApi
import net.phadata.billing.utils.AuthCenterUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component


/**
 * 实现
 * @author linx
 * @since 2022-07-21 09:20
 *
 */
@Component
class BillingServerApiImpl : BaseServerApi(), BillingServerApi {

    @Autowired
    lateinit var authCenterServerApi: AuthCenterServerApi

    private val url = ""

    @Value("\${account-platform.address}")
    var address = ""

    override fun notifyBilling(params: NotifyBillingRequest): Boolean? {
        return this.notifyBilling(buildUrl(address, url), params)
    }

    override fun notifyBilling(url: String?, params: NotifyBillingRequest): Boolean? {
        if (url == null) {
            throw ServiceException("url为空")
        }
        val httpHeaders = HttpHeaders().apply {
            add(AuthCenterUtil.TOKEN, authCenterServerApi.createToken())
        }
        val httpEntity = HttpEntity(params, httpHeaders)
        val resultStr = restTemplate.postForObject(url, httpEntity, String::class.java)
        val buildApiResult = buildApiResult(resultStr, Boolean::class.java)
        if (buildApiResult.code != "200000") {
            throw ServiceException("通知失败:${buildApiResult}")
        }
        return buildApiResult.payload
    }


}