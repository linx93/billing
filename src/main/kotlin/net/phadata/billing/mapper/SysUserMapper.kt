package net.phadata.billing.mapper;

import net.phadata.billing.model.po.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@Mapper
interface SysUserMapper : BaseMapper<SysUser>
