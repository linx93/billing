package net.phadata.billing.model.order

import io.swagger.annotations.ApiModelProperty
import net.phadata.billing.constant.PayTypeEnum
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


/**
 * 订单保存请求
 * @author linx
 * @since 2022-07-20 14:39
 *
 */
class OrderSaveRequest {

    @NotBlank(message = "订单id不能为空")
    @ApiModelProperty(value = "订单id")
    var orderId: String? = null

    @NotBlank(message = "平台名称不能为空")
    @ApiModelProperty(value = "平台名称")
    var platformName: String? = null

    @NotBlank(message = "平台代码不能为空")
    @ApiModelProperty(value = "平台代码")
    var platformCode: String? = null

    @NotNull(message = "平台代码不能为空")
    @ApiModelProperty(value = "创建时间")
    var createTime: Long? = null

    @ApiModelProperty(value = "更新时间")
    var updateTime: Long? = null

    @NotNull(message = "金额不能为空")
    @ApiModelProperty(value = "金额")
    var amount: BigDecimal? = null

    @ApiModelProperty(value = "描述信息")
    var descInfo: String? = null

    @NotNull(message = "支付时间不能为空")
    @ApiModelProperty(value = "支付时间")
    var payTime: Long? = null

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    var consumerName: String? = null

    @NotBlank(message = "客户code不能为空")
    @ApiModelProperty(value = "客户code或者说是客户id，客户的唯一标识")
    var consumerCode: String? = null

    @NotBlank(message = "客户企业社会统一信用代码不能为空")
    @ApiModelProperty(value = "客户企业社会统一信用代码，开普票的时候使用")
    var consumerCompanyCode: String? = null

    //@NotBlank(message = "支付类型不能为空")
    @ApiModelProperty(value = "支付类型")
    var payType: PayTypeEnum? = null
}