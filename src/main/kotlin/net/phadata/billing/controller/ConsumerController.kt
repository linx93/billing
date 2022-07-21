package net.phadata.billing.controller;


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.consumer.ConsumerQueryPage
import net.phadata.billing.model.consumer.ConsumerResponse
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.po.OrderRecords
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam
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
@Api(tags = ["客户相关"], description = "客户相关")
@RestController
@RequestMapping("/api/v1/consumer")
class ConsumerController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService

    @ApiOperation(value = "客户分页查询", notes = "客户分页查询")
    @PostMapping("page")
    fun consumerQueryPage(
        @RequestBody consumerQueryPage: ConsumerQueryPage,
        response: HttpServletResponse
    ): ApiResult<PageInfo<ConsumerResponse>> {
        val pageInfo = orderRecordsService.pageByConsumerQueryPage(consumerQueryPage)
        return ApiResult.success(pageInfo)
    }

    @ApiOperation(value = "客户详情[keyword就传客户名称]", notes = "客户详情")
    @PostMapping("details")
    fun consumerDetails(@RequestBody consumerQueryPage: ConsumerQueryPage): ApiResult<PageInfo<OrderResponse>> {
        val result: PageInfo<OrderResponse> = orderRecordsService.consumerDetails(consumerQueryPage)
        return ApiResult.success(result)
    }
}

