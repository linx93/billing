package net.phadata.billing.service;

import net.phadata.billing.model.po.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import net.phadata.billing.model.login.LoginRequest
import net.phadata.billing.model.login.LoginResponse
import net.phadata.billing.model.login.ModifyPassword
import net.phadata.billing.model.register.RegisterRequest

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
interface SysUserService : IService<SysUser> {
    fun login(loginRequest: LoginRequest): LoginResponse
    fun register(registerRequest: RegisterRequest): Boolean
    fun modifyPassword(modifyPassword: ModifyPassword): Boolean?
}
