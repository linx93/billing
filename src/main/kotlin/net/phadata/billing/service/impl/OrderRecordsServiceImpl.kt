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
import net.phadata.billing.model.order.ConsumerQuery
import net.phadata.billing.model.order.ConsumerQueryPage
import net.phadata.billing.model.order.OrderQuery
import net.phadata.billing.model.order.OrderResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;

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
            orderQuery.timeRange != null && orderQuery.timeRange!!.startTime != null && orderQuery.timeRange!!.endTime != null,
            OrderRecords::payTime,
            orderQuery.timeRange!!.startTime,
            orderQuery.timeRange!!.endTime
        )
        val selectList = getBaseMapper().selectList(ktQueryWrapper)
        return orderConverter.toDownloadOrderList(selectList)
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

    override fun pageByConsumerQueryPage(consumerQueryPage: ConsumerQueryPage): PageInfo<OrderResponse> {
        val like = KtQueryWrapper(OrderRecords()).like(
            consumerQueryPage.keyword != null,
            OrderRecords::consumerName,
            consumerQueryPage.keyword
        )
        val selectPage = getBaseMapper().selectPage(
            Page(
                consumerQueryPage.page.current.toLong(), consumerQueryPage.page.size.toLong()
            ), like
        )
        return PageInfo<OrderResponse>().apply {
            current = selectPage.current
            size = selectPage.size
            total = selectPage.total
            records = orderConverter.toOrderResponse(selectPage.records)
        }
    }

}

