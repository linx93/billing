package net.phadata.billing.model.login

import io.swagger.annotations.ApiModelProperty

class LoginResponse {

    @ApiModelProperty(value = "用户信息")
    var userInfo: UserInfo? = null

    @ApiModelProperty(value = "token")
    var token: String? = null

}
