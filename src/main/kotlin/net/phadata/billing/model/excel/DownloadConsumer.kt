package net.phadata.billing.model.excel

import com.alibaba.excel.annotation.ExcelProperty
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal

/**
 * 下载客户EXCEL
 * @author linx
 * @since 2022-06-14 00:31
 */
class DownloadConsumer {
    @ExcelProperty("订单id")
    var orderId: String? = null

    @ExcelProperty("平台名称")
    var platformName: String? = null

    @ExcelProperty("金额")
    var amount: BigDecimal? = null

    @ExcelProperty("描述信息")
    var descInfo: String? = null

    @ExcelProperty("支付时间")
    var payTime: Long? = null

    @ExcelProperty("客户名称")
    var consumerName: String? = null

    @ExcelProperty("开票状态[0：未开票、1：开票中、2：已开票]")
    var billingStatus: String? = null

    @ExcelProperty("支付类型")
    var payType: String? = null
}