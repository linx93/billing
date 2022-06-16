package net.phadata.billing.model.register

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

class RegisterRequest {
    @NotBlank(message = "昵称或者姓名不能为空")
    @ApiModelProperty(value = "昵称或者姓名")
    var nickName: String? = null

    @NotBlank(message = "登录账户不能为空")
    @ApiModelProperty(value = "登录账户")
    var account: String? = null

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    var password: String? = null
}