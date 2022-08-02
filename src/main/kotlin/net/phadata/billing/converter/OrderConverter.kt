package net.phadata.billing.converter

import net.phadata.billing.model.consumer.ConsumerResponse
import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.order.OrderSaveRequest
import net.phadata.billing.model.po.OrderRecords
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * 关于订单的映射
 * @author linx
 * @since 2022-06-16 23:52
 *
 */
@Mapper(componentModel = "spring")
abstract class OrderConverter {
    @Mappings(
        value = [
            //Mapping(target = "billingUrl", ignore = true),
            Mapping(
                source = "orderRecords.payTime",
                target = "payTime",
                qualifiedByName = ["toDateString"],
            ),
            Mapping(
                source = "orderRecords.billingStatus",
                target = "billingStatus",
                qualifiedByName = ["parseBillingStatus"],
            ),
            Mapping(
                source = "orderRecords.payType",
                target = "payType",
                qualifiedByName = ["parsePayType"],
            )
        ]
    )
    abstract fun toDownloadOrder(orderRecords: OrderRecords): DownloadOrder

    abstract fun toDownloadOrderList(orderRecordsList: List<OrderRecords>): List<DownloadOrder>

    abstract fun toDownloadConsumer(orderRecords: OrderRecords): DownloadConsumer

    abstract fun toDownloadConsumerList(orderRecords: List<ConsumerResponse>): List<DownloadConsumer>

    abstract fun toOrderResponse(records: List<OrderRecords>): List<OrderResponse>

    abstract fun toConsumerResponse(records: List<OrderRecords>): List<ConsumerResponse>

    abstract fun toOrderRecords(orderSaveRequest: OrderSaveRequest): OrderRecords

    @Named("parseBillingStatus")
    protected fun parseBillingStatus(billingStatus: Int): String {
        //开票状态[0：未开票、1：开票中、2：已开票]
        return when (billingStatus) {
            0 -> "未开票"
            1 -> "开票中"
            2 -> "已开票"
            else -> "错误"
        }
    }

    @Named("parsePayType")
    protected fun parsePayType(payType: String): String {
        //支付类型  alipay, wxpay, paypal
        return when (payType) {
            "alipay" -> "支付宝"
            "wxpay" -> "微信"
            "paypal" -> "paypal"
            else -> "未知"
        }
    }


    @Named("toDateString")
    protected fun toDateString(p: java.lang.Long): String {
        val ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return ofPattern.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(p.toLong()), ZoneId.systemDefault()))
    }


}