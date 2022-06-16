package net.phadata.billing.converter

import net.phadata.billing.model.login.UserInfo
import net.phadata.billing.model.po.SysUser
import org.mapstruct.Mapper


/**
 * 关于用户信息的映射
 * @author linx
 * @since 2022-06-13 20:52
 *
 */
@Mapper(componentModel = "spring")
interface UserInfoConverter {
    fun toUserInfo(sysUser: SysUser): UserInfo

}
