package net.phadata.billing.service.impl;

import net.phadata.billing.model.po.OrderRecords;
import net.phadata.billing.mapper.OrderRecordsMapper;
import net.phadata.billing.service.OrderRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
