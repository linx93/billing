package net.phadata.billing.model.excel

import com.alibaba.excel.annotation.ExcelProperty
import java.math.BigDecimal

/**
 * 下载订单EXCEL
 * @author linx
 * @since 2022-06-14 00:31
 */
class DownloadOrder {
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

    @ExcelProperty("开票状态")
    var billingStatus: String? = null

    @ExcelProperty("票据地址")
    var billingUrl: String? = null

    @ExcelProperty("支付类型")
    var payType: String? = null


    override fun toString(): String {
        return "DownloadOrder(orderId=$orderId, platformName=$platformName, amount=$amount, descInfo=$descInfo, payTime=$payTime, consumerName=$consumerName, billingStatus=$billingStatus, billingUrl=$billingUrl, payType=$payType)"
    }


}