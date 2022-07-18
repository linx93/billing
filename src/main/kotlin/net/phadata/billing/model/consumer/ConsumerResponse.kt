package net.phadata.billing.model.consumer

import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal


/**
 * 客户响应
 * @author linx
 * @since 2022-07-18 16:11
 *
 */
class ConsumerResponse {
    @ApiModelProperty(value = "订单id")
    var orderId: String? = null


    @ApiModelProperty(value = "金额")
    var amountSum: BigDecimal? = null


    @ApiModelProperty(value = "客户名称")
    var consumerName: String? = null

    @ApiModelProperty(value = "客户企业社会统一信用代码，开普票的时候使用")
    var consumerCompanyCode: String? = null

}