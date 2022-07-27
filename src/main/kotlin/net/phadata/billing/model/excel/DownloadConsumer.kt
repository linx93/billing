package net.phadata.billing.model.excel

import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.write.style.ColumnWidth
import com.alibaba.excel.annotation.write.style.HeadRowHeight
import java.math.BigDecimal


/**
 * 下载客户EXCEL
 * @author linx
 * @since 2022-06-14 00:31
 */
@HeadRowHeight(20)
class DownloadConsumer {

    @ColumnWidth(35)
    @ExcelProperty("客户")
    var consumerName: String? = null

    @ColumnWidth(35)
    @ExcelProperty("客户证件号")
    var consumerCompanyCode: String? = null

    @ColumnWidth(15)
    @ExcelProperty("付费总金额")
    var amountSum: BigDecimal? = null


    override fun toString(): String {
        return "DownloadConsumer(amountSum=$amountSum, consumerName=$consumerName, consumerCompanyCode=$consumerCompanyCode)"
    }


}