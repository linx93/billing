package net.phadata.billing.controller.foreignservice;


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.billing.ConfirmBillingRequest
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.model.order.OrderSaveRequest
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid

/**
 * <p>
 * 订单记录表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Api(tags = ["对外服务"], description = "对外服务")
@RestController
@RequestMapping("/api/v1/foreign-service")
class OrderRecordsController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService


    @ApiOperation(value = "接收支付完成的订单数据【供数字账户调用】", notes = "接收支付完成的订单数据【供数字账户调用】")
    @PostMapping("save")
    fun save(@Valid @RequestBody orderSaveRequest: OrderSaveRequest): ApiResult<Boolean> {
        val save: Boolean = orderRecordsService.saveOrder(orderSaveRequest)
        return ApiResult.success(save)
    }


    @ApiOperation(value = "确认开票", notes = "确认开票")
    @PostMapping("confirm-billing")
    fun confirmBilling(@Valid @RequestBody confirmBillingRequest: ConfirmBillingRequest): ApiResult<Boolean> {
        val save: Boolean = orderRecordsService.confirmBilling(confirmBillingRequest)
        return ApiResult.success(save)
    }


}

