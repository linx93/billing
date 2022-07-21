package net.phadata.billing.service;

import net.phadata.billing.model.po.OrderRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.billing.ConfirmBillingRequest
import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.consumer.ConsumerQuery
import net.phadata.billing.model.consumer.ConsumerQueryPage
import net.phadata.billing.model.consumer.ConsumerResponse
import net.phadata.billing.model.order.OrderQuery
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.order.OrderSaveRequest
import net.phadata.billing.model.statistics.DonutChart
import org.springframework.web.multipart.MultipartFile

/**
 * <p>
 * 订单记录表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
interface OrderRecordsService : IService<OrderRecords> {
    fun listByOrderQuery(orderQuery: OrderQuery): List<DownloadOrder>
    fun pageByOrderQueryPage(orderQueryPage: OrderQueryPage): PageInfo<OrderResponse>
    fun listByConsumerQuery(consumerQuery: ConsumerQuery): List<DownloadConsumer>
    fun pageByConsumerQueryPage(consumerQueryPage: ConsumerQueryPage): PageInfo<ConsumerResponse>
    fun platformPayProp(): List<DonutChart>
    fun payAmountProp(): List<DonutChart>
    fun saveOrder(orderSaveRequest: OrderSaveRequest): Boolean
    fun confirmBilling(confirmBillingRequest: ConfirmBillingRequest): Boolean
    fun upload(file: MultipartFile, id: Long): Boolean?
}
