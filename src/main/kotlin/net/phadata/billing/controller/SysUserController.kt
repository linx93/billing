package net.phadata.billing.controller;


import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.model.login.LoginRequest
import net.phadata.billing.model.login.LoginResponse
import net.phadata.billing.model.register.RegisterRequest
import net.phadata.billing.service.SysUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Api(tags = ["登录"], description = "登录")
@RestController
@RequestMapping("/api/v1/sys")
class SysUserController {
    @Autowired
    private lateinit var sysUserService: SysUserService


    @PostMapping("login")
    @ApiOperation(value = "登录", notes = "登录")
    fun login(@RequestBody @Validated loginRequest: LoginRequest): ApiResult<LoginResponse?> {
        val loginResponse: LoginResponse = sysUserService.login(loginRequest)
        return ApiResult.success(loginResponse)
    }

    @PostMapping("register")
    @ApiOperation(value = "注册", notes = "注册")
    fun register(@RequestBody @Validated registerRequest: RegisterRequest): ApiResult<Boolean?> {
        val registerResponse = sysUserService.register(registerRequest)
        return ApiResult.success(registerResponse)
    }
}

