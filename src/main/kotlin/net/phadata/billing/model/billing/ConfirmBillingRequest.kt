package net.phadata.billing.model.billing

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull


/**
 * 确认开票请求
 * @author linx
 * @since 2022-07-20 16:05
 *
 */
class ConfirmBillingRequest {
    @NotNull(message = "订单id不能为空")
    @ApiModelProperty(value = "订单id")
    var orderId: String? = null

    @NotNull(message = "开具状态通知地址不能为空")
    @ApiModelProperty(value = "开具状态通知地址")
    var notifyUrl: String? = null
}