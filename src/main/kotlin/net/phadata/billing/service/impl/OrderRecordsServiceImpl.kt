package net.phadata.billing.service.impl;

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import net.phadata.billing.model.po.OrderRecords;
import net.phadata.billing.mapper.OrderRecordsMapper;
import net.phadata.billing.service.OrderRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.phadata.billing.converter.OrderConverter
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.consumer.ConsumerQuery
import net.phadata.billing.model.consumer.ConsumerQueryPage
import net.phadata.billing.model.consumer.ConsumerResponse
import net.phadata.billing.model.order.OrderQuery
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.statistics.DonutChart
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;
import java.math.BigDecimal
import kotlin.math.ceil

/**
 * <p>
 * 订单记录表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Service
open class OrderRecordsServiceImpl : ServiceImpl<OrderRecordsMapper, OrderRecords>(), OrderRecordsService {
    @Autowired
    lateinit var orderConverter: OrderConverter
    override fun listByOrderQuery(orderQuery: OrderQuery): List<DownloadOrder> {
        val ktQueryWrapper = KtQueryWrapper(OrderRecords()).eq(
            StringUtils.isNotBlank(orderQuery.platformCode), OrderRecords::platformCode, orderQuery.platformCode
        ).and(StringUtils.isNotBlank(orderQuery.keyword)) {
            it.like(
                OrderRecords::orderId, orderQuery.keyword
            ).or().like(
                OrderRecords::consumerName, orderQuery.keyword
            )
        }.between(
            orderQuery.timeRange?.startTime != null && orderQuery.timeRange?.endTime != null,
            OrderRecords::payTime,
            orderQuery.timeRange?.startTime,
            orderQuery.timeRange?.endTime
        )
        return orderConverter.toDownloadOrderList(getBaseMapper().selectList(ktQueryWrapper))
    }

    override fun pageByOrderQueryPage(orderQueryPage: OrderQueryPage): PageInfo<OrderResponse> {
        val ktQueryWrapper = KtQueryWrapper(OrderRecords()).eq(
            StringUtils.isNotBlank(orderQueryPage.platformCode), OrderRecords::platformCode, orderQueryPage.platformCode
        ).and(StringUtils.isNotBlank(orderQueryPage.keyword)) {
            it.like(
                OrderRecords::orderId, orderQueryPage.keyword
            ).or().like(
                OrderRecords::consumerName, orderQueryPage.keyword
            )
        }.between(
            orderQueryPage.timeRange?.startTime != null && orderQueryPage.timeRange?.endTime != null,
            OrderRecords::payTime,
            orderQueryPage.timeRange?.startTime,
            orderQueryPage.timeRange?.endTime
        )
        val selectPage = getBaseMapper().selectPage(
            Page(
                orderQueryPage.page.current.toLong(), orderQueryPage.page.size.toLong()
            ), ktQueryWrapper
        )
        return PageInfo<OrderResponse>().apply {
            current = selectPage.current.toInt()
            size = selectPage.size.toInt()
            total = selectPage.total.toInt()
            records = orderConverter.toOrderResponse(selectPage.records)
        }
    }

    override fun listByConsumerQuery(consumerQuery: ConsumerQuery): List<DownloadConsumer> {
        val like = KtQueryWrapper(OrderRecords()).like(
            consumerQuery.keyword != null,
            OrderRecords::consumerName,
            consumerQuery.keyword
        )
        val selectList = getBaseMapper().selectList(like)
        return orderConverter.toDownloadConsumerList(selectList)
    }

    override fun pageByConsumerQueryPage(consumerQueryPage: ConsumerQueryPage): PageInfo<ConsumerResponse> {
        val like = KtQueryWrapper(OrderRecords()).like(
            consumerQueryPage.keyword != null,
            OrderRecords::consumerName,
            consumerQueryPage.keyword
        )
        val groupBy = getBaseMapper().selectList(like).groupBy(OrderRecords::consumerName)
        val consumerResponseList = mutableListOf<ConsumerResponse>()
        groupBy.forEach { element ->
            var amountSum = BigDecimal("0")
            var consumerName: String? = ""
            var consumerCompanyCode: String? = ""
            element.value.forEach { item ->
                amountSum = amountSum.add(item.amount)
                consumerName = item.consumerName
                consumerCompanyCode = item.consumerCompanyCode
            }
            consumerResponseList.add(ConsumerResponse().apply {
                this.consumerName = consumerName
                this.consumerCompanyCode = consumerCompanyCode
                this.amountSum = amountSum
            })
        }
        //分页计算
        var total = consumerResponseList.size
        var pageSize = consumerQueryPage.page.size
        var current = consumerQueryPage.page.current - 1
        //一共的页数
        var pageTotal = ceil(total.toDouble() / pageSize).toInt()
        if (pageTotal > 0) {
            pageTotal -= 1
        }
        var to: Int = if (total % pageSize == 0) {
            current * pageSize + pageSize
        } else {
            if (current >= pageTotal) {
                current * pageSize + (total % pageSize)
            } else {
                current * pageSize + pageSize
            }
        }
        val start: Int = current * pageSize
        if (current >= pageTotal) {

        }
        val slice = if (current >= pageTotal) {
            arrayListOf<ConsumerResponse>()
        } else {
            consumerResponseList.slice(IntRange(start, to - 1))
        }
        return PageInfo<ConsumerResponse>().apply {
            this.current = current + 1
            this.size = pageSize
            this.total = total
            this.records = slice
        }
    }

    /**
     * 本月各平台付费占比
     */
    override fun platformPayProp(): List<DonutChart> {
        return getBaseMapper().platformPayProp()
    }

    override fun payAmountProp(): List<DonutChart> {
        return getBaseMapper().payAmountProp()
    }

}

