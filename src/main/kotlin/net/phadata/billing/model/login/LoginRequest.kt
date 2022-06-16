package net.phadata.billing.model.login

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
class LoginRequest(
    @NotBlank(message = "登录账户不能为空")
    @ApiModelProperty(value = "登录账户")
    var account: String,

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    var password: String,
)
