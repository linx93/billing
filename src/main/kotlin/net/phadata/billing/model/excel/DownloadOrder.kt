package net.phadata.billing.model.excel

import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.write.style.ColumnWidth
import com.alibaba.excel.annotation.write.style.HeadRowHeight
import java.math.BigDecimal

/**
 * 下载订单EXCEL
 * @author linx
 * @since 2022-06-14 00:31
 */
@ColumnWidth(33)
@HeadRowHeight(20)
class DownloadOrder {
    @ExcelProperty("订单id")
    var orderId: String? = null

    @ExcelProperty("平台名称")
    var platformName: String? = null

    @ColumnWidth(15)
    @ExcelProperty("金额")
    var amount: BigDecimal? = null

    @ExcelProperty("支付时间")
    var payTime: String? = null

    @ColumnWidth(20)
    @ExcelProperty("支付类型")
    var payType: String? = null

    @ExcelProperty("客户名称")
    var consumerName: String? = null

    @ColumnWidth(20)
    @ExcelProperty("开票状态")
    var billingStatus: String? = null

    @ExcelProperty("票据地址")
    var billingUrl: String? = null


    @ColumnWidth(100)
    @ExcelProperty("描述信息")
    var descInfo: String? = null

    override fun toString(): String {
        return "DownloadOrder(orderId=$orderId, platformName=$platformName, amount=$amount, descInfo=$descInfo, payTime=$payTime, consumerName=$consumerName, billingStatus=$billingStatus, billingUrl=$billingUrl, payType=$payType)"
    }


}