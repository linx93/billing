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
import net.phadata.billing.service.OrderRecordsService
import net.phadata.billing.task.AsyncNotifyBillingTask
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
    private val log = LoggerFactory.getLogger(OrderRecordsServiceImpl::class.java)

    @Autowired
    lateinit var orderConverter: OrderConverter

    @Autowired
    lateinit var minioClientConfig: MinioClientConfig

    @Autowired
    lateinit var minioUtil: MinioUtil

    @Autowired
    lateinit var asyncNotifyBillingTask: AsyncNotifyBillingTask

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
        val total = consumerResponseList.size
        val pageSize = consumerQueryPage.page.size
        val current = consumerQueryPage.page.current - 1
        //一共的页数
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

    override fun saveOrder(orderSaveRequest: OrderSaveRequest): Boolean {
        val orderId = orderSaveRequest.orderId
        if (getBaseMapper().selectList(KtQueryWrapper(OrderRecords()).eq(OrderRecords::orderId, orderId))
                .isNotEmpty()
        ) {
            throw ClientException(ResultCode.DATA_ALREADY_EXISTS)
        }
        val orderRecords = orderConverter.toOrderRecords(orderSaveRequest)
        return save(orderRecords)
    }

    override fun confirmBilling(confirmBillingRequest: ConfirmBillingRequest): Boolean {
        val selectList = getBaseMapper().selectList(
            KtQueryWrapper(OrderRecords()).eq(
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
    override fun upload(file: MultipartFile, id: Long): Boolean {
        CompletableFuture.supplyAsync {
            //1. 上传
            return@supplyAsync upload(file)
        }.thenCombine(CompletableFuture.supplyAsync {
            //2. 查询
            return@supplyAsync getById(id)
        }) { url: String, byId: OrderRecords ->
            byId.updateTime = Instant.now().epochSecond
            byId.billingUrl = url
            //默认成功
            byId.notifyStatus = 2
            //更新db
            val saveOrUpdate = saveOrUpdate(byId)
            if (saveOrUpdate) {
                //通知
                val notifyUrl = byId.notifyUrl
                //3. 通知更新开票状态
                val apply = NotifyBillingRequest().apply {
                    byId.orderId
                    BillingStatusEnum.INVOICED.code
                    byId.billingUrl
                }
                try {
                    val notifyResult = asyncNotifyBillingTask.notifyBilling(notifyUrl, apply)
                    if (notifyResult) {
                        log.error("SUCCESS-通知数字账户更新开票状态成功:${apply}")
                    } else {
                        log.error("ERROR-通知数字账户更新开票状态失败:${apply}")
                        //更新通知状态为失败 票据状态通知状态[0:未通知 1:通知成功 2:通知失败]
                        updateFailStatus(byId.id, 2)
                    }
                } catch (e: Exception) {
                    //请求失败
                    log.error("ERROR-通知数字账户更新开票状态失败:${apply}")
                    //更新通知状态为失败 票据状态通知状态[0:未通知 1:通知成功 2:通知失败]
                    updateFailStatus(byId.id, 2)
                }
            }
        }
        return true
    }

    override fun consumerDetails(consumerQueryPage: ConsumerQueryPage): PageInfo<OrderResponse> {
        val selectPage = getBaseMapper().selectPage(
            Page(consumerQueryPage.page.current.toLong(), consumerQueryPage.page.size.toLong()),
            KtQueryWrapper(OrderRecords()).eq(OrderRecords::consumerName, consumerQueryPage.keyword)
        )
        return PageInfo<OrderResponse>().apply {
            current = selectPage.current.toInt()
            size = selectPage.size.toInt()
            total = selectPage.total.toInt()
            records = orderConverter.toOrderResponse(selectPage.records)
        }
    }

    private fun updateFailStatus(id: Long?, status: Int) {
        getBaseMapper().updateById(OrderRecords().apply {
            this.id = id
            this.notifyStatus = status
        })
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

