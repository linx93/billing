package net.phadata.billing.converter

import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.po.OrderRecords
import org.mapstruct.Mapper


/**
 * 关于订单的映射
 * @author linx
 * @since 2022-06-16 23:52
 *
 */
@Mapper(componentModel = "spring")
interface OrderConverter {
    fun toDownloadOrder(orderRecords: OrderRecords): DownloadOrder

    fun toDownloadOrderList(orderRecordsList: List<OrderRecords>): List<DownloadOrder>

    fun toDownloadConsumer(orderRecords: OrderRecords): DownloadConsumer

    fun toDownloadConsumerList(orderRecords: List<OrderRecords>): List<DownloadConsumer>

    fun toOrderResponse(records: List<OrderRecords>): List<OrderResponse>


}