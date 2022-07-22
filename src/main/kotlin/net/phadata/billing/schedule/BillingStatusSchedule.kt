package net.phadata.billing.schedule

import org.springframework.scheduling.annotation.Scheduled


/**
 * 发票状态定时监测
 * @author linx
 * @since 2022-07-22 17:56
 *
 */
class BillingStatusSchedule {
    @Scheduled(cron = "0/5 * * * * ?")
    fun billingStatus(){
        //todo 更新同步失败状态的开发票的记录
    }

}