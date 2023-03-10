package net.phadata.billing.model.billing

import io.swagger.annotations.ApiModelProperty


/**
 * 通知开票请求
 * @author linx
 * @since 2022-07-21 10:55
 *
 */
class NotifyBillingRequest {

    @ApiModelProperty(value = "订单id")
    var orderId: String? = null

    @ApiModelProperty(value = "开票状态[0：未开票、1：开票中、2：已开票]")
    var billingStatus: Int? = null

    @ApiModelProperty(value = "发票的地址")
    var billingUrl: String? = null

    override fun toString(): String {
        return "NotifyBillingRequest(orderId='$orderId', billingStatus=$billingStatus, billingUrl='$billingUrl')"
    }
}