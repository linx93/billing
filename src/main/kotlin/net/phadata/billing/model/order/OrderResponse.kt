package net.phadata.billing.model.order

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal

class OrderResponse {
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer::class)
    var id: Long? = null

    @ApiModelProperty(value = "订单id")
    var orderId: String? = null

    @ApiModelProperty(value = "平台名称")
    var platformName: String? = null

    @ApiModelProperty(value = "金额")
    var amount: BigDecimal? = null

    @ApiModelProperty(value = "描述信息")
    var descInfo: String? = null

    @ApiModelProperty(value = "支付时间")
    var payTime: Long? = null

    @ApiModelProperty(value = "客户名称")
    var consumerName: String? = null

    @ApiModelProperty(value = "客户企业社会统一信用代码，开普票的时候使用")
    var consumerCompanyCode: String? = null

    @ApiModelProperty(value = "开票状态[0：未开票、1：开票中、2：已开票]")
    var billingStatus: Int? = null

    @ApiModelProperty(value = "支付类型")
    var payType: String? = null
}