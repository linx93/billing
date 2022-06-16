package net.phadata.billing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import net.phadata.billing.converter.UserInfoConverter
import net.phadata.billing.utils.AssertUtil
import net.phadata.billing.mapper.SysUserMapper
import net.phadata.billing.model.login.LoginRequest
import net.phadata.billing.model.login.LoginResponse
import net.phadata.billing.model.po.SysUser
import net.phadata.billing.service.SysUserService
import net.phadata.billing.utils.BCryptPasswordEncoder
import net.phadata.billing.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Service
class SysUserServiceImpl : ServiceImpl<SysUserMapper, SysUser>(), SysUserService {
    @Autowired
    private lateinit var userInfoConverter: UserInfoConverter
    override fun login(loginRequest: LoginRequest): LoginResponse {
        val account = loginRequest.account
        val password = loginRequest.password
        val eq = KtQueryWrapper(SysUser()).eq(SysUser::account, account)
        val sysUser = getBaseMapper().selectOne(eq)
        AssertUtil.notNull(sysUser, "用户不存在")
        AssertUtil.isTrue(BCryptPasswordEncoder().matches(loginRequest.password, password), "用户名密码错误")
        val toUserInfo = userInfoConverter.toUserInfo(sysUser)
        var tokenStr = JwtUtil.create(toUserInfo.userId.toString(), toUserInfo)
        return LoginResponse().apply {
            userInfo = toUserInfo
            token = tokenStr
        }
    }

}
