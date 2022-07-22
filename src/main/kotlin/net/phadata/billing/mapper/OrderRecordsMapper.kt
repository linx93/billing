package net.phadata.billing.mapper;

import net.phadata.billing.model.po.OrderRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.phadata.billing.model.statistics.CommonVO
import net.phadata.billing.model.statistics.DonutChart
import net.phadata.billing.model.statistics.IdText
import org.apache.ibatis.annotations.Mapper

/**
 * <p>
 * 订单记录表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Mapper
interface OrderRecordsMapper : BaseMapper<OrderRecords> {
    fun platformPayProp(): List<DonutChart>
    fun payAmountProp(): List<DonutChart>
    fun platformGroup(): List<IdText>
    fun platformPayTrend(platformText: String):List<CommonVO>
    fun payCustomerTrend(platformText: String): List<CommonVO>
    //fun pageByConsumerQueryPage(consumerQueryPage: ConsumerQueryPage): List<ConsumerResponse>
    //fun total(consumerQueryPage: ConsumerQueryPage): Int
}

