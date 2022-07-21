package net.phadata.billing.network

import net.phadata.billing.model.billing.NotifyBillingRequest


/**
 * 开票相关请求
 * @author linx
 * @since 2022-07-21 09:19
 *
 */
interface BillingServerApi {

    /**
     * 通知开票成功
     */
    fun notifyBilling(params: NotifyBillingRequest): Boolean?

    /**
     * 通知开票成功
     */
    fun notifyBilling(url: String, params: NotifyBillingRequest): Boolean?


}