package net.phadata.billing.service.impl;

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import net.phadata.billing.converter.UserInfoConverter
import net.phadata.billing.utils.AssertUtil
import net.phadata.billing.mapper.SysUserMapper
import net.phadata.billing.model.login.LoginRequest
import net.phadata.billing.model.login.LoginResponse
import net.phadata.billing.model.po.SysUser
import net.phadata.billing.model.register.RegisterRequest
import net.phadata.billing.service.SysUserService
import net.phadata.billing.utils.BCryptPasswordEncoder
import net.phadata.billing.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

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
        val eq = KtQueryWrapper(SysUser()).eq(SysUser::account, loginRequest.account)
        val sysUser = getBaseMapper().selectOne(eq)
        AssertUtil.notNull(sysUser, "用户名或密码错误")
        AssertUtil.isTrue(BCryptPasswordEncoder().matches(loginRequest.password, sysUser.password), "用户名或密码错误")
        val toUserInfo = userInfoConverter.toUserInfo(sysUser)
        var tokenStr = JwtUtil.create(toUserInfo.userId.toString(), toUserInfo)
        return LoginResponse().apply {
            userInfo = toUserInfo
            token = tokenStr
        }
    }

    override fun register(registerRequest: RegisterRequest): Boolean {
        //判断重复
        AssertUtil.isEmpty(
            getBaseMapper().selectList(
                KtQueryWrapper(SysUser()).eq(
                    SysUser::account,
                    registerRequest.account
                )
            ), "【${registerRequest.account}】账户已存在"
        )
        //执行注册
        val toSysUser = userInfoConverter.toSysUser(registerRequest)
        val apply = toSysUser.apply {
            createTime = Instant.now().epochSecond
            updateTime = Instant.now().epochSecond
            roleId = 0
            password = BCryptPasswordEncoder().encode(password)
        }
        //insert
        getBaseMapper().insert(apply)
        return true
    }

}
