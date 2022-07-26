package net.phadata.billing.controller;


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.app.common.TjPlatformEnum
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.order.OrderSaveRequest
import net.phadata.billing.model.po.OrderRecords
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
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
@Api(tags = ["订单相关"], description = "订单相关")
@RestController
@RequestMapping("/api/v1/order-records")
class OrderRecordsController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService

    @ApiOperation(value = "订单分页查询", notes = "订单分页查询")
    @PostMapping("page")
    fun consumerQueryPage(@RequestBody orderQueryPage: OrderQueryPage): ApiResult<PageInfo<OrderResponse>?> {
        val pageInfo: PageInfo<OrderResponse> = orderRecordsService.pageByOrderQueryPage(orderQueryPage)
        return ApiResult.success(pageInfo)
    }

    @ApiOperation(value = "根据主键id查询", notes = "根据主键id查询")
    @GetMapping("find")
    fun getById(@RequestParam id: Long): ApiResult<OrderRecords> {
        val byId = orderRecordsService.getById(id)
        return ApiResult.success(byId)
    }


    @ApiOperation(value = "天机各平台列表", notes = "天机各平台列表")
    @GetMapping("platform-list")
    fun platformList(): ApiResult<List<Map<String, String>>> {
        val listOf = mutableListOf<Map<String, String>>()
        TjPlatformEnum.values().forEach { tjPlatformEnum ->
            val mapOf = mutableMapOf<String, String>()
            //mapOf[tjPlatformEnum.code] = tjPlatformEnum.getName()
            mapOf["code"] = tjPlatformEnum.code
            mapOf["name"] = tjPlatformEnum.getName()
            listOf.add(mapOf)
        }
        return ApiResult.success(listOf)
    }


}

