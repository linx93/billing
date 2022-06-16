package net.phadata.billing.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@ApiModel(value="SysUser对象", description="系统用户表")
class SysUser : Serializable {

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    var userId: Int? = null
    @ApiModelProperty(value = "昵称或者姓名")
    var nickName: String? = null
    @ApiModelProperty(value = "登录账户")
    var account: String? = null
    var createTime: Long? = null
    var updateTime: Long? = null
    @ApiModelProperty(value = "密码")
    var password: String? = null
    @ApiModelProperty(value = "角色id，预留后续扩展使用")
    var roleId: Int? = null


    override fun toString(): String {
        return "SysUser{" +
        "userId=" + userId +
        ", nickName=" + nickName +
        ", account=" + account +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", password=" + password +
        ", roleId=" + roleId +
        "}"
    }
}
