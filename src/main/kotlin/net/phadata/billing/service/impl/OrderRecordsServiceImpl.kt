package net.phadata.billing.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import net.phadata.billing.configuration.MinioClientConfig
import net.phadata.billing.constant.BillingStatusEnum
import net.phadata.billing.constant.ResultCode
import net.phadata.billing.converter.OrderConverter
import net.phadata.billing.exception.ClientException
import net.phadata.billing.mapper.OrderRecordsMapper
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.billing.ConfirmBillingRequest
import net.phadata.billing.model.billing.NotifyBillingRequest
import net.phadata.billing.model.consumer.ConsumerQuery
import net.phadata.billing.model.consumer.ConsumerQueryPage
import net.phadata.billing.model.consumer.ConsumerResponse
import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.order.OrderQuery
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.order.OrderSaveRequest
import net.phadata.billing.model.po.OrderRecords
import net.phadata.billing.model.statistics.DonutChart
import net.phadata.billing.model.statistics.Polyline
import net.phadata.billing.model.statistics.SeriesData
import net.phadata.billing.service.OrderRecordsService
import net.phadata.billing.asynctask.AsyncNotifyBillingTask
import net.phadata.billing.exception.ServiceException
import net.phadata.billing.utils.MinioUtil
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import java.util.concurrent.CompletableFuture
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
class OrderRecordsServiceImpl : ServiceImpl<OrderRecordsMapper, OrderRecords>(), OrderRecordsService {

    private val loggger = LoggerFactory.getLogger(OrderRecordsServiceImpl::class.java)

    @Autowired
    lateinit var orderConverter: OrderConverter

    @Autowired
    lateinit var minioClientConfig: MinioClientConfig

    @Autowired
    lateinit var minioUtil: MinioUtil

    @Autowired
    lateinit var asyncNotifyBillingTask: AsyncNotifyBillingTask

    override fun listByOrderQuery(orderQuery: OrderQuery): List<DownloadOrder> {
        val ktQueryWrapper = KtQueryWrapper(OrderRecords::class.java).eq(
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
        val ktQueryWrapper = KtQueryWrapper(OrderRecords::class.java).eq(
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
        ).orderByDesc(OrderRecords::createTime)
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
        val like = KtQueryWrapper(OrderRecords::class.java).like(
            StringUtils.isNotBlank(consumerQuery.keyword),
            OrderRecords::consumerName,
            consumerQuery.keyword
        )
        val selectList = getBaseMapper().selectList(like)
        return orderConverter.toDownloadConsumerList(selectList)
    }

    override fun pageByConsumerQueryPage(consumerQueryPage: ConsumerQueryPage): PageInfo<ConsumerResponse> {
        val like = KtQueryWrapper(OrderRecords::class.java).like(
            StringUtils.isNotBlank(consumerQueryPage.keyword),
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
        consumerResponseList.sortByDescending { item -> item.amountSum }
        //分页计算
        val total = consumerResponseList.size
        val pageSize = consumerQueryPage.page.size
        val current = consumerQueryPage.page.current
        /*//一共的页数
        var pageTotal = ceil(total.toDouble() / pageSize).toInt()
        if (pageTotal > 0) {
            pageTotal -= 1
        }
        val to: Int = if (total % pageSize == 0) {
            current * pageSize + pageSize
        } else {
            if (current >= pageTotal) {
                current * pageSize + (total % pageSize)
            } else {
                current * pageSize + pageSize
            }
        }
        val start: Int = current * pageSize
        val slice = if (current >= pageTotal) {
            arrayListOf()
        } else {
            consumerResponseList.slice(IntRange(start, to - 1))
        }*/
        var slice = mutableListOf<ConsumerResponse>()
        if (pageSize >= total) {
            slice = consumerResponseList
        } else {
            val start: Int = (current - 1) * pageSize
            val end: Int = current * pageSize
            for ((index, value) in consumerResponseList.withIndex()) {
                //println("the element at $index is $value")
                if (index in start until end) {
                    slice.add(value)
                }
            }
        }
        return PageInfo<ConsumerResponse>().apply {
            this.current = current
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

    override fun saveOrder(orderSaveRequest: OrderSaveRequest): Boolean {
        loggger.info("同步订单请求:${orderSaveRequest}")
        val orderId = orderSaveRequest.orderId
        if (getBaseMapper().selectList(KtQueryWrapper(OrderRecords::class.java).eq(OrderRecords::orderId, orderId))
                .isNotEmpty()
        ) {
            throw ClientException(ResultCode.DATA_ALREADY_EXISTS)
        }
        val orderRecords = orderConverter.toOrderRecords(orderSaveRequest)
        orderRecords.notifyStatus = 0
        orderRecords.billingStatus = 0
        return save(orderRecords)
    }

    override fun confirmBilling(confirmBillingRequest: ConfirmBillingRequest): Boolean {
        loggger.info("确认开票请求:${confirmBillingRequest}")
        val selectList = getBaseMapper().selectList(
            KtQueryWrapper(OrderRecords::class.java).eq(
                OrderRecords::orderId,
                confirmBillingRequest.orderId
            )
        )
        if (selectList.isEmpty()) {
            throw ClientException(ResultCode.DATA_DOES_NOT_EXIST)
        }
        if (selectList.size > 1) {
            throw ClientException(ResultCode.DATA_IS_NOT_UNIQUE)
        }
        val orderRecords = selectList[0]
        orderRecords.billingStatus = BillingStatusEnum.INVOICING.code
        orderRecords.updateTime = Instant.now().epochSecond
        orderRecords.notifyUrl = confirmBillingRequest.notifyUrl
        return saveOrUpdate(orderRecords)
    }

    @Transactional
    override fun upload(file: MultipartFile, id: Long): String {
        /*CompletableFuture.supplyAsync {
            //1. 上传
            return@supplyAsync upload(file)
        }.thenCombine(CompletableFuture.supplyAsync {
            //2. 查询
            return@supplyAsync getById(id)
        }) { url: String, byId: OrderRecords ->
            byId.updateTime = Instant.now().epochSecond
            byId.billingUrl = url
            //更新db
            val saveOrUpdate = saveOrUpdate(byId)
        }*/
        val url = upload(file)
        val byId = getById(id)
        byId.updateTime = Instant.now().epochSecond
        byId.billingUrl = url
        //更新db
        val saveOrUpdate = saveOrUpdate(byId)
        return url
    }

    override fun consumerDetails(consumerQueryPage: ConsumerQueryPage): PageInfo<OrderResponse> {
        val selectPage = getBaseMapper().selectPage(
            Page(consumerQueryPage.page.current.toLong(), consumerQueryPage.page.size.toLong()),
            KtQueryWrapper(OrderRecords::class.java).eq(OrderRecords::consumerName, consumerQueryPage.keyword)
        )
        return PageInfo<OrderResponse>().apply {
            current = selectPage.current.toInt()
            size = selectPage.size.toInt()
            total = selectPage.total.toInt()
            records = orderConverter.toOrderResponse(selectPage.records)
        }
    }

    override fun platformPayTrend(): Polyline {
        val baseMapper = getBaseMapper()
        val platformIdText = baseMapper.platformGroup()
        val polyline = Polyline()
        val list = platformIdText.map { idText -> idText.text }
        polyline.legendData = list
        val seriesDataList = mutableListOf<SeriesData>()
        list.forEach {
            val result = baseMapper.platformPayTrend(it)
            polyline.buildSeriesDataList(seriesDataList, it, result)
        }
        polyline.series = seriesDataList
        polyline.xAxisData = polyline.getMonthList(6)
        return polyline
    }


    override fun payCustomerTrend(): Polyline {
        val baseMapper = getBaseMapper()
        val platformIdText = baseMapper.platformGroup()
        val polyline = Polyline()
        val list = platformIdText.map { idText -> idText.text }
        polyline.legendData = list
        val seriesDataList = mutableListOf<SeriesData>()
        list.forEach {
            val result = baseMapper.payCustomerTrend(it)
            polyline.buildSeriesDataList(seriesDataList, it, result)
        }
        polyline.series = seriesDataList
        polyline.xAxisData = polyline.getMonthList(6)
        return polyline
    }

    @Transactional
    override fun confirmNotify(id: Long): Boolean {
        val byId = getBaseMapper().selectById(id)
        //通知
        val notifyUrl = byId.notifyUrl
        byId.notifyStatus = 1
        byId.billingStatus = 2
        updateById(byId)
        //3. 通知更新开票状态
        val apply = NotifyBillingRequest().apply {
            this.orderId = byId.orderId
            this.billingStatus = BillingStatusEnum.INVOICED.code
            this.billingUrl = byId.billingUrl
        }
        try {
            val notifyResult = asyncNotifyBillingTask.notifyBilling(notifyUrl, apply)
            if (notifyResult) {
                loggger.info("SUCCESS-通知数字账户更新开票状态成功:${apply}")
            } else {
                loggger.error("ERROR-通知数字账户更新开票状态失败:${apply}")
                //更新通知状态为失败 票据状态通知状态[0:未通知 1:通知成功 2:通知失败]
                getBaseMapper().updateById(OrderRecords().apply {
                    this.id = id
                    this.notifyStatus = 2
                })
                throw ServiceException("ERROR-通知数字账户更新开票状态失败:${apply}")
            }
        } catch (e: Exception) {
            //请求失败
            loggger.error("ERROR-通知数字账户更新开票状态失败:${e.localizedMessage}")
            //更新通知状态为失败 票据状态通知状态[0:未通知 1:通知成功 2:通知失败]
            getBaseMapper().updateById(OrderRecords().apply {
                this.id = id
                this.notifyStatus = 2
            })
            throw ServiceException("ERROR-通知数字账户更新开票状态失败:${apply}")
        }
        return true
    }


    private fun upload(file: MultipartFile): String {
        val nameList = minioUtil.upload(arrayOf(file))
        val fileUrlList: MutableList<String> = LinkedList()
        for (name in nameList) {
            fileUrlList.add((minioClientConfig.endpoint + minioClientConfig.bucketName) + "/" + name)
        }
        return fileUrlList[0]
    }

}

