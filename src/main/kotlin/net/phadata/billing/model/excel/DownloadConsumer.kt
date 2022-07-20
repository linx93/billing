package net.phadata.billing.model.excel

import com.alibaba.excel.annotation.ExcelProperty
import java.math.BigDecimal


/**
 * 下载客户EXCEL
 * @author linx
 * @since 2022-06-14 00:31
 */
class DownloadConsumer {

    @ExcelProperty("客户")
    var consumerName: String? = null

    @ExcelProperty("客户证件号")
    var consumerCompanyCode: String? = null

    @ExcelProperty("付费总金额")
    var amountSum: BigDecimal? = null


    override fun toString(): String {
        return "DownloadConsumer(amountSum=$amountSum, consumerName=$consumerName, consumerCompanyCode=$consumerCompanyCode)"
    }


}