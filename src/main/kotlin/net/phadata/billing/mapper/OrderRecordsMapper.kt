package net.phadata.billing.mapper;

import net.phadata.billing.model.po.OrderRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
interface OrderRecordsMapper : BaseMapper<OrderRecords>

