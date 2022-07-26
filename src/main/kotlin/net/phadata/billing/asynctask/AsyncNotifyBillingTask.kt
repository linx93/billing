package net.phadata.billing.asynctask

import net.phadata.billing.model.billing.NotifyBillingRequest
import net.phadata.billing.network.BillingServerApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


/**
 * 异步通知开票成功
 * @author linx
 * @since 2022-07-21 14:58
 *
 */
@Component
class AsyncNotifyBillingTask {
    @Autowired
    lateinit var billingServerApi: BillingServerApi


    @Async
    fun notifyBilling(notifyUrl: String?, notifyBillingRequest: NotifyBillingRequest): Boolean {
        return billingServerApi.notifyBilling(notifyUrl, notifyBillingRequest) == true
    }
}