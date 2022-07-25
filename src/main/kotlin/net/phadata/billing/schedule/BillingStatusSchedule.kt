package net.phadata.billing.schedule

import com.baomidou.mybatisplus.core.toolkit.Wrappers
import net.phadata.billing.mapper.OrderRecordsMapper
import net.phadata.billing.model.po.OrderRecords
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


/**
 * 发票状态定时监测
 * @author linx
 * @since 2022-07-22 17:56
 *
 */
@Component
class BillingStatusSchedule {
    lateinit var orderRecordsMapper: OrderRecordsMapper
    private val log = LoggerFactory.getLogger(BillingStatusSchedule::class.java)

    @Scheduled(cron = "0 0 2 * * ?")
    fun billingStatus() {
        val list = orderRecordsMapper.selectList(Wrappers.emptyWrapper())
        list.forEach {
            //发票地址不为空且通知失败
            if (it.billingUrl != null && it.notifyStatus == 2) {
                //更新为通知成功
                orderRecordsMapper.updateById(OrderRecords().apply {
                    id = it.id
                    notifyStatus = it.notifyStatus
                })
                //log.debug("timed-task 定时任务更新同步失败状态的开发票的记录开始")
                log.info("timed-task 同步失败状态的开发票的记录:${it}")
                //log.debug("timed-task 定时任务更新同步失败状态的开发票的记录结束")
            }
        }
    }

}