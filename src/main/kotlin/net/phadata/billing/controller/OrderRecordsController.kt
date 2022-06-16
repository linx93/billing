package net.phadata.billing.controller;


import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
class OrderRecordsController

