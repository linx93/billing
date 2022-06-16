package net.phadata.billing.controller;


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.order.ConsumerQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse

/**
 * <p>
 * 订单记录表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Api(tags = ["订单相关"], description = "订单相关")
@RestController
@RequestMapping("/api/v1/order-records")
class OrderRecordsController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService
    @ApiOperation(value = "客户分页查询", notes = "订单分页查询")
    @PostMapping("order-page")
    fun consumerQueryPage(
        @RequestBody consumerQueryPage: ConsumerQueryPage,
        response: HttpServletResponse
    ): ApiResult<PageInfo<OrderResponse>?> {
        val pageInfo: PageInfo<OrderResponse> = orderRecordsService.pageByConsumerQueryPage(consumerQueryPage)
        return ApiResult.success(pageInfo)
    }
}

