package net.phadata.billing.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.statistics.DonutChart
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 统计控制器
 * @author linx
 * @since 2022-07-19 15:26
 *
 */
@Api(tags = ["首页统计相关"], description = "首页统计相关")
@RestController
@RequestMapping("/api/v1/statistics")
class StatisticsController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService

    @ApiOperation(value = "本月各平台付费占比", notes = "本月各平台付费占比")
    @GetMapping("platform-pay-prop")
    fun platformPayProp(): ApiResult<List<DonutChart>> {
        val data: List<DonutChart> = orderRecordsService.platformPayProp()
        return ApiResult.success(data)
    }

    @ApiOperation(value = "本月付费金额占比", notes = "本月各平台付费占比")
    @GetMapping("pay-amount-prop")
    fun payAmountProp(): ApiResult<List<DonutChart>> {
        val data: List<DonutChart> = orderRecordsService.payAmountProp()
        return ApiResult.success(data)
    }

    @ApiOperation(value = "各平台付费趋势", notes = "各平台付费趋势")
    @GetMapping("platform-pay-trend")
    fun platformPayTrend(): ApiResult<Any> {
        //val data: List<DonutChart> = orderRecordsService.platformPayTrend()
        //todo 各平台付费趋势
        return ApiResult.success("null")
    }

    @ApiOperation(value = "付费客户数量趋势", notes = "付费客户数量趋势")
    @GetMapping("pay-customer-trend")
    fun payCustomerTrend(): ApiResult<Any> {
        //todo 付费客户数量趋势
        return ApiResult.success("data")
    }


}