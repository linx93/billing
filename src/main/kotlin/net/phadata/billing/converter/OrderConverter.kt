package net.phadata.billing.converter

import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.po.OrderRecords
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named


/**
 * 关于订单的映射
 * @author linx
 * @since 2022-06-16 23:52
 *
 */
@Mapper(componentModel = "spring")
abstract class OrderConverter {
    abstract fun toDownloadOrder(orderRecords: OrderRecords): DownloadOrder

    abstract fun toDownloadOrderList(orderRecordsList: List<OrderRecords>): List<DownloadOrder>

    @Mappings(
        value = [
            //Mapping(target = "description",ignore = true),
            Mapping(source = "orderRecords.billingStatus", target = "billingStatus", qualifiedByName = ["parseBillingStatus"])
        ]
    )
    abstract fun toDownloadConsumer(orderRecords: OrderRecords): DownloadConsumer

    abstract fun toDownloadConsumerList(orderRecords: List<OrderRecords>): List<DownloadConsumer>

    abstract fun toOrderResponse(records: List<OrderRecords>): List<OrderResponse>


    @Named("parseBillingStatus")
    protected fun parseBillingStatus(billingStatus: Int): String? {
        //开票状态[0：未开票、1：开票中、2：已开票]
        return when (billingStatus) {
            0 -> "未开票"
            1 -> "开票中"
            2 -> "已开票"
            else -> "错误"
        }
    }


}