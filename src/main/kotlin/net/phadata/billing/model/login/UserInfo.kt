package net.phadata.billing.model.login

import io.swagger.annotations.ApiModelProperty


/**
 * 用户信息
 * @author linx
 * @since 2022-06-13 23:53
 *
 */
class UserInfo {
    @ApiModelProperty(value = "用户id")
    var userId: Int? = null

    @ApiModelProperty(value = "昵称或者姓名")
    var nickName: String? = null

    @ApiModelProperty(value = "登录账户")
    var account: String? = null
    // var createTime: Long? = null
    //var updateTime: Long? = null

    @ApiModelProperty(value = "角色id，预留后续扩展使用")
    var roleId: Int? = null


    override fun toString(): String {
        return "UserInfo(userId=$userId, nickName=$nickName, account=$account, roleId=$roleId)"
    }


}