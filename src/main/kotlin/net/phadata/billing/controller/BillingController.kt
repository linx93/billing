package net.phadata.billing.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.base.PageInfo
import net.phadata.billing.model.order.OrderQueryPage
import net.phadata.billing.model.order.OrderResponse
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


/**
 * 票据相关接口
 * @author linx
 * @since 2022-07-20 16:50
 *
 */
@Api(tags = ["票据相关"], description = "票据相关")
@RestController
@RequestMapping("/api/v1/billing")
class BillingController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService

    @ApiOperation(value = "上传发票[pdf格式的发票]", notes = "上传发票[pdf格式的发票]")
    @PostMapping("upload")
    fun uploadBilling(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("id") id: Long
    ): ApiResult<Boolean> {
        return ApiResult.success(orderRecordsService.upload(file, id))
    }

    @ApiOperation(value = "确认并通知", notes = "确认并通知")
    @GetMapping("confirm-notify")
    fun confirmNotify(@RequestParam("id") id: Long): ApiResult<Boolean> {
        return ApiResult.success(orderRecordsService.confirmNotify(id))
    }
}